package lab3

class ChildThread(no: Int) extends Thread
{


  override def run=
  {
    val n = no
    val m = this.getId()
    println(s"child: $n $m")
  }
}

