package com.josiahebhomenye

/**
  * Created by jay on 22/05/2016.
  */
object TypeTest extends App{

	implicit class RichSeq(underlying: Seq[Int]){

		def +(other: Seq[Int]) : Seq[Int] = for(i <- underlying.indices) yield underlying(i) + other(i)
	}

	val res = Seq(1, 2, 3) + Seq(4, 5, 6)
	println(res.mkString(" "))

}
