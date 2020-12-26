package lab5
import java.text.DecimalFormat

object GLEstimator extends App {

  val n: Int = 100000000
  val start: Float = System.nanoTime()

  val Pi: Double = (1 to n).foldLeft(1.0)((PiOverFour, n) =>
    PiOverFour + (1.0 - (2.0 * (n.toDouble % 2.0)))/(2.0 * n.toDouble + 1.0))

  val result = Pi * 4.0

  val end: Float = System.nanoTime()
  val runtime = (end - start)/1000000000
  println("Pi = " + result)
  println(f"td = $runtime%1.2f s")

}
