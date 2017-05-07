package scalaadaptive.core.runtime

/**
  * Created by pk250187 on 5/7/17.
  */
class TrainingHelper(val runner: FunctionRunner) {
  def train[TReturnType](functions: Seq[ReferencedFunction[TReturnType]],
                         inputDescriptor: Long): Unit = {
    functions.foreach { f =>
      runner.runOption(List(f), inputDescriptor)
    }
  }
}
