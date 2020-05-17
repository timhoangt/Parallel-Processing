//Timothy Hoang

package lab2

/*
Creates a list of numbers, filters out the even numbers,
adds them all up, and multiplies the result by 2.
 */
object SeqMultAdder extends App {

  val nums = List(1, 3, 4, 5, 12, 2, 7, 9, 7)

  //val f = { n: Int => println(n) }
  def f(n: Int): Unit = println(n)

  val odds = nums.filter{_%2 == 1}
  odds.foreach(println(_))
  //nums.foreach(println(_))

  val total = odds.foldLeft(0) { (sum, odd) => sum + 2*odd }
  println(total)

  val lista = List(1,2,3,4,5,6,7,8,9,10)
  val listb = lista.filter (_ % 2 == 1)
  println(listb)
  val listc = listb.map{ _ * 3}
  println(listc)
}
