package lab6

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.math._

object FuturesGLEstimator extends App {

  val RANGE = 1000000L
  val max = Int.MaxValue
  val realPart: Float = max.toFloat / RANGE.toFloat
  val NUM_PARTITIONS = ceil(realPart).toInt
  val start: Float = System.nanoTime()

  val futures = for(k <- 0L until NUM_PARTITIONS) yield Future {
    //compute lower for this future based on k and RANGE
    val lower = (k * RANGE) + 1
    //compute upper for this future based on k and RANGE
    val upper = min(k * RANGE + RANGE, max)
    //sum in (lower,upper) range per Eq. 1
    val sum: Double = (lower to upper).foldLeft(1.0)((PiOverFour, upper) =>
      PiOverFour + (1 - (2.0 * (upper.toDouble % 2.0))) / (2.0 * upper.toDouble + 1.0))

    println(sum)
    sum * 4 - 4
  }

  val pi = 4 + futures.foldLeft(0.0) { (sum, future) =>
    import scala.concurrent.duration._
    val pi = Await.result(future, 100 seconds)
    sum + pi
  }

  val end: Float = System.nanoTime()
  val runtime = (end - start)/1000000000
  val N = Runtime.getRuntime.availableProcessors/2
  val TN: Double = (runtime*0.90/N) + (0.1*runtime)
  val R: Double =  runtime / TN
  val e: Double = R / N

  println("Pi = " + pi)
  println(f"T1 = $runtime%1.2f")
  println(f"TN = $TN%1.2f")
  println("N = " + N)
  println(f"R = $R%1.2f")
  println(f"e = $e%1.2f")
}
