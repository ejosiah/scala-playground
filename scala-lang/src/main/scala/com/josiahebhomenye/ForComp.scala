package com.josiahebhomenye

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Josiah on 5/22/2016.
  */
class ForComp{

  implicit val ec: ExecutionContext = new ExecutionContext {
    override def reportFailure(cause: Throwable): Unit = throw cause

    override def execute(runnable: Runnable): Unit = runnable.run()
  }

  def twoSeqs(): Unit ={

  }
}
