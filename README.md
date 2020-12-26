# Parallel Processing

Video walkthough of this repository can be found here

Excercises and programs that utlize parallel processing programming practices.

Created in IntelliJ for simplicity when running, make sure you follow the .idea file configurations.

Make sure you install ParaScale this can be done by following the following steps.

1. Download the latest Parascale with 
```
git clone https://github.com/roncoleman125/ParaScale.git
```

2. Navigate to your project or create a new one in IntelliJ and go to File > Project Structure... > Libraries >  (in middle panel) > Scala SDK > Download… >
Version 2.12.8 > Choose Modules > select parascale and parascale-build

3. File > Project Structure > Project: make sure 1.8 JDK set

4.  File > Project Structure > Modules > parascale-build: select sbt: sbt-and-plugins then － to delete it.

5. Dialoge boxes and banner might open up when attempting to run ParentWithThread in the parascale.thread.basic package. Just dont change them.

# Project
Everything here can be found in the Parallel Processing Project folder within /src

Analyzed, designed, and built a small parallel computing cluster of three hosts utilizing multicore and multiprocessor parallel programming to analyze 100,000 bond portfolios.

## assign1
Explores the scale-out capacity of actors running on multiple hosts.

To run this program to its full potential, you need two hosts connected on a LAN.
The hosts must have the ability to open ports outside the well-known range, 0-1024.

It is possible to test and run on one host but the point is to use two hosts so that we can expand to use combined resources.

To run, run ActorB until it waits for a connection. Then, run ActorA and it should send payload Y to ActorB.

## assign2
Scales-out the perfect number finder (PNF) using the dispatcher-worker design pattern on multiple hosts and analyze the speed up.

To run this program to its full potential, you need two hosts connected on a LAN.
The hosts must have the ability to open ports outside the well-known range, 0-1024.

It is possible to test and run on one host but the point is to use two hosts so that we can expand to use combined resources.

To run, run PerfectWorker until it waits for a connection. Then, run PerfectDispatcher and it should send pertitions to PerfectWorker.

The log in tmp within the c: folder should show all tasks dispatched and worked on.

## assign3

Analyzes all 100,000 portfolios in the ParaBond inventory using a cluster of three
hosts.

Once again, it is possible to do on a single host but the point is to run it on multiple machines to speed things up. This program should take a while for each partition. Do not worry if it takes a few minutes.

Make sure you run the worker before the dispatcher.

# Labs
Everything here can be found in the Parallel Processing Labs folder within /src

## lab1
Just a HelloWorld program to test making project in IntelliJ

## lab2
Utilize some of Scala's higher-order functions: foreach, filter and foldLeft.

## lab3
Start working with threads. ParentWithThread.scala spawns and waits for a child thread and ParentWithRunnable.scala spawns and joins as many child runnables.

## lab5
GLEstimator is a sequential Scala application. It estimates 𝜋, using the Gregory-Leibniz series.

## lab6
FuturesGLEstimator is a parallel Scala program that estimates 𝜋 using
Gregory-Leibniz (GL) series and futures.

## lab7
ParGLEstimator is a parallel Scala program that estimates 𝜋 using Gregory-Leibniz (GL) series nad parallel collections.
