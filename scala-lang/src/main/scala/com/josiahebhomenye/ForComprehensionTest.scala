package com.josiahebhomenye

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Josiah on 5/22/2016.
  */
object ForComprehensionTest extends App with CurrentThreadExecutionContext {

  val list = Seq("Hello", "to", "day", "is", "a", "new", "day")

  val res = for{
    s <- list
    c <- s
    i = c.toInt if i % 2 == 0
  } yield i

  println(res)

  val res1 = list.flatMap(s => s.toSeq.withFilter(_.toInt%2 == 0).map(_.toInt) )
  println(res1)

  val res2 = for{
    a <- Future(10/2)
    b <- Future(a + 1)
    c <- Future(a - 1)
  } yield b * c
  println(res2)

  val res3 = Future(10/2).flatMap{ a =>  Future(a + 1).map( b => b * (a - 1)) }

  println(res3)

}
