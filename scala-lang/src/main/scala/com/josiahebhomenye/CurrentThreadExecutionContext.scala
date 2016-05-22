package com.josiahebhomenye

import scala.concurrent.ExecutionContext

/**
  * Created by Josiah on 5/22/2016.
  */
trait CurrentThreadExecutionContext extends ExecutionContext{

  implicit val ec: ExecutionContext = this

  override def execute(runnable: Runnable): Unit = runnable.run()

  override def reportFailure(cause: Throwable): Unit = throw cause
}
