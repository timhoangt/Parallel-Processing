package lab3

object ParentWithThread extends App {
  val numCores = Runtime.getRuntime.availableProcessors()
  val child = new Thread(new ChildThread(0))

  child.start()

  val numThreads = Thread.activeCount

  val threadID = Thread.currentThread.getId()

  //output the number of cores, active threads, and the thread id.
  println(s" $numCores $numThreads $threadID")

  child.join

}
