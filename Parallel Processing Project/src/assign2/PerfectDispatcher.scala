package assign2

import org.apache.log4j.Logger
import parascale.actor.last.{Dispatcher, Task}
import parascale.util._
import scala.math._
import java.io._


/**
 * Spawns a dispatcher to connect to multiple workers.
 */
object PerfectDispatcher extends App {
  val LOG = Logger.getLogger(getClass)
  LOG.info("started")

  // For initial testing on a single host, use this socket.
  // When deploying on multiple hosts, use the VM argument,
  // -Dsocket=<ip address>:9000 which points to the second
  // host.
  val socket2 = getPropertyOrElse("socket","localhost:9000")

  // Construction forks a thread which automatically runs the actor act method.
  new PerfectDispatcher(List("localhost:8000", socket2))
}

/**
 * Template dispatcher which tests readiness of
 * @param sockets
 */
class PerfectDispatcher(sockets: List[String]) extends Dispatcher(sockets) {
  import PerfectDispatcher._

  /**
   * Handles actor startup after construction.
   */
  def act: Unit = {
    //LOG.info("sockets to workers = "+sockets)

    /*
    (0 until sockets.length).foreach { k =>
      LOG.info("sending message to worker " + k)
      workers(k) ! "to worker(" + k + ") hello from dispatcher"
    }
    */

    // TODO: Replace the code below to implement PNF
    // Create the partition info and put it in two separate messages,
    // one for each worker, then wait below for two replies, one from
    // each worker
    println("PNF using Futures")
    println("March 29, 2020")
    println("Timothy Hoang")
    val N = Runtime.getRuntime.availableProcessors
    println(N)
    println("Candidate         Perfect         T1          TN          R          e")
    var j = 0

    val candidates: List[Long] =
    List(
      6,
      28,
      496,
      8128,
      33550336,
      33550336+1,
      8589869056L+1,
      8589869056L,
      137438691328L)
    for (e <- candidates) yield {
      val candidate = e



      (0 until sockets.length).foreach { k =>

        val lower = (k * candidate / 2) + 1
        val upper = min(lower + (candidate / 2), candidate)

        workers(k) ! Partition(lower, upper, candidate)
      }
      var numReplies = 0
      var finalSum: Long = 0
      var isPerfect = false
      var totalRuntime: Float = 0
      while (numReplies < 2)
      // This while loop wait forever but we really just need to wait
      // for two replies, one from each worker. The result, that is,
      // the partial sum and the elapsed times are in the payload as
      // a Result class.
        receive match {
          case task: Task if task.kind == Task.REPLY =>
            task.payload match {
              case result: Result =>

                if (numReplies == 0) {
                  finalSum = finalSum + result.sum
                  val t1 = result.t1
                  val t0 = result.t0
                  val runtimeWorker: Float = (t1 - t0) / 1000000000
                  totalRuntime = totalRuntime + runtimeWorker
                  println("first sum = " + result.sum)
                }
                else {
                  finalSum = finalSum + result.sum
                  val t11 = result.t1
                  val t01 = result.t0
                  val runtimeWorker1: Float  = (t11 - t01) / 1000000000
                  totalRuntime = totalRuntime + runtimeWorker1
                  println("second sum = " + result.sum)
                }
                numReplies = numReplies + 1
                if (numReplies == 2) {
                  isPerfect = (finalSum % candidate) == 0
                  val TN: Double = (totalRuntime * 0.90 / N) + (0.1 * totalRuntime)
                  val R: Double = totalRuntime / TN
                  val e: Double = R / N
                  println("final sum = " + finalSum)
                  println(candidate + "        " + isPerfect + "        " + totalRuntime +
                    "         " + f"$TN%1.2f" + "         " + f"$R%1.2f" + "         " + f"$e%1.2f")
                }
            }
            j = j + 1
        }
    }
  }
}
