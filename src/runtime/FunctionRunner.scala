package runtime

/**
  * Created by pk250187 on 3/26/17.
  */
trait FunctionRunner {
  def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]], byValue: Int = 0): TReturnType
}
