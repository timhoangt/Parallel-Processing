package assign2

import org.apache.log4j.Logger
import parascale.actor.last.{Task, Worker}
import parascale.util._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import java.io._


/**
 * Spawns workers on the localhost.
 */
object PerfectWorker extends App {
  val LOG = Logger.getLogger(getClass)

  LOG.info("started")

  // Number of hosts in this configuration
  val nhosts = getPropertyOrElse("nhosts",1)

  // One-port configuration
  val port1 = getPropertyOrElse("port", 8000)

  // If there is just one host, then the ports will include 9000 by default
  // Otherwise, if there are two hosts in this configuration, use just one
  // port which must be specified by VM options
  val ports = if(nhosts == 1) List(port1, 9000) else List(port1)

  // Spawn the worker(s).
  // Note: for initial testing with a single host, "ports" contains two ports.
  // When deploying on two hosts, "ports" will contain one port per host.
  for(port <- ports) {
    // Construction forks a thread which automatically runs the actor act method.
    new PerfectWorker(port)
  }
}

/**
 * Template worker for finding a perfect number.
 * @param port Localhost port this worker listens to
 */
class PerfectWorker(port: Int ) extends Worker(port) {
  import PerfectWorker._

  /**
   * Handles actor startup after construction.
   */
  override def act: Unit = {
    val name = getClass.getSimpleName
    LOG.info("started " + name + " (id=" + id + ")")

    // Wait for inbound messages as tasks
    while (true) {
      receive match {
        // TODO: Replace the code below to implement PNF
        // It gets the partition range info from the task payload then
        // spawns futures (or uses parallel collections) to analyze the
        // partition in parallel. Finally, when done, it replies
        // with the partial sum and the time elapsed time.



        case task: Task  =>
          task.payload match {
            case partition: Partition =>
              val start = partition.start
              val end = partition.end
              val candidate = partition.candidate
              val RANGE = 1000000L
              val numPartitions = ((end.toDouble-start.toDouble) / RANGE).ceil.toInt
              val timeStart = System.nanoTime()

              val futures = for(k <- 0L until numPartitions) yield Future {
                val lower: Long = k * RANGE + 1

                val upper: Long = candidate min (k + 1) * RANGE

                sumOfFactorsInRange_(lower, upper, candidate)
              }

              val total = futures.foldLeft(0L) { (sum, future) =>
                import scala.concurrent.duration._
                val result = Await.result(future, 100000 seconds)

                sum + result
              }
              val timeEnd = System.nanoTime()


              sleep(500)
              sender ! Result(total, timeStart, timeEnd)




          }
          def sumOfFactorsInRange_(lower: Long, upper: Long, number: Long): Long = {
            var index: Long = lower

            var sum = 0L

            while(index <= upper) {
              if(number % index == 0L)
                sum += index

              index += 1L
            }
            sum
          }
          // Send a simple reply to test the connectivity.
        case that =>
          LOG.warn("got unexpected message: "+that)

      }
    }
  }
}
