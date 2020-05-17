package lab3

object ParentWithRunnable extends App {
  val numCores = Runtime.getRuntime.availableProcessors()
  val children = for(no <- 0 until numCores) yield {
    val child = new Thread(new ChildRunnable(no))
    child.start()
    child
  }
  val numThreads = Thread.activeCount
  val threadID = Thread.currentThread.getId()
  println(s"Number of cores: $numCores Active Threads: $numThreads ThreadID: $threadID")

  children.foreach(_.join())

}