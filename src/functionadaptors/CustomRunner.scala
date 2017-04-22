package functionadaptors

import options.Storage
import options.Storage.Storage
import runtime.{Adaptive, FunctionRunner, ReferencedFunction}
import runtime.history.HistoryStorage

/**
  * Created by pk250187 on 4/22/17.
  */
trait CustomRunner extends FunctionRunner {
  protected val storage: Storage

  private val runner: FunctionRunner = storage match {
    case Storage.Local => Adaptive.createRunner()
    case _ => null
  }

  def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]], byValue: Int = 0): TReturnType =
    if (runner != null) runner.runOption(options, byValue)
    else Adaptive.runner.runOption(options, byValue)
}
