package assign3

import org.apache.log4j.Logger
import parascale.actor.last.{Dispatcher, Task}
import parascale.util._
import scala.math._
import parabond.cluster._
import java.io._


/**
 * Spawns a dispatcher to connect to multiple workers.
 */
object ParaDispatcher extends App {
  val LOG = Logger.getLogger(getClass)
  LOG.info("started")

  // For initial testing on a single host, use this socket.
  // When deploying on multiple hosts, use the VM argument,
  // -Dsocket=<ip address>:9000 which points to the second
  // host.
  val socket2 = getPropertyOrElse("socket","localhost:9000")

  // Construction forks a thread which automatically runs the actor act method.
  new ParaDispatcher(List("localhost:8000", socket2))
}

/**
 * Template dispatcher which tests readiness of
 * @param sockets
 */
class ParaDispatcher(sockets: List[String]) extends Dispatcher(sockets) {
  import ParaDispatcher._
  import parabond.cluster._
  val node = new CoarseGrainedNode

  def act: Unit = {

    println("ParaBond Analysis")
    println("By Timothy Hoang")
    println("April 25, 2020")
    println("CoarseGrainedNode")
    println("Workers: 2")
    println("Hosts: localhost (dispatcher), localhost (worker)")
    val N = Runtime.getRuntime.availableProcessors
    println("Cores: " + N)
    println("N        missed        T1        TN        R        e")

    val ladder = List(
      1000,
      2000,
      4000,
      8000,
      16000,
      32000,
      64000,
      100000 )

    (0 until ladder.length).foreach { k =>

      val rung = ladder(k)
      val checkThis = checkReset(ladder(k),0)
      workers(0) ! Partition(rung/2, 0)
      workers(1) ! Partition(rung/2, rung/2)
      var numReplies = 0
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
                  totalRuntime = result.t1
                }
                else {
                  totalRuntime = (totalRuntime + result.t1) / 1000000000F
                }
                numReplies = numReplies + 1
                if (numReplies == 2) {
                  val TN: Double = (totalRuntime * 0.90 / N) + (0.1 * totalRuntime)
                  val R: Double = totalRuntime / TN
                  val e: Double = R / N
                  println( rung + "         "  + f"$totalRuntime%1.2f" + "         " + f"$TN%1.2f" + "         " + f"$R%1.2f" + "         " + f"$e%1.2f")
                }
            }
        }
      System.out.println("missed " + check(checkThis).length)

    }


  }
}