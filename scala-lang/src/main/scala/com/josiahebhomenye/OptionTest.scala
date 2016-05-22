package com.josiahebhomenye

import scala.util.{Failure, Success, Try}

/**
  * Created by Josiah on 5/22/2016.
  */
object OptionTest extends App {


  var optStr: Option[String] = Option("Hello we have some thing")
  optStr foreach println
  val str = optStr.get
  println(str)

  val optStr2 = Option("Another String")

  val optStr3 = optStr ++ optStr2
  val noStr = Option.empty[String]

  println(s"$optStr ++ $optStr2 = $optStr3")

  val res0 = optStr ++ Seq("One", "two", "three")

  println(s"head of exitings string ${optStr2.head}")

  println(s"zip with index ${optStr.zipWithIndex}")

  Try( noStr.head ) match {
    case Success(head) => println(s"has a value $head")
    case Failure(_) => println("No value found")
  }

  val res1 = noStr getOrElse "No value found using getOrElse"
  println(res1)

  val res2 = (noStr fold "No value found using fold") (identity)
  println(res2)


}
