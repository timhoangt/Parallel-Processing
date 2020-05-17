package lab3

class ChildRunnable(no: Int) extends Runnable {
  override def run=
  {
    val n = no
    val m = Thread.currentThread.getId()
    println(s"child: $n $m")
  }
}
