package lab7

import scala.language.postfixOps
import scala.math._

object ParGLEstimator extends App {
  val RANGE = 1000000L
  val max = Int.MaxValue
  val realPart: Float = max.toFloat / RANGE.toFloat
  val numPartitions = ceil(realPart).toInt
  val start: Float = System.nanoTime()

  val ranges = for(k <- 0 to numPartitions) yield {
    val lower = (k * RANGE) + 1
    val upper = min(k * RANGE + RANGE, max)
    (lower, upper)
  }

  val partials = ranges.par.map { partial =>
    val (lower, upper) = partial
    // sum over lower and upper per Equation
    val sum: Double = (lower to upper).foldLeft(1.0)((PiOverFour, upper) =>
      PiOverFour + (1 - (2.0 * (upper.toDouble % 2.0))) / (2.0 * upper.toDouble + 1.0))

    sum * 4 - 4
  }



  val pi = 4 + partials.foldLeft(0.0)(_ + _)

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
