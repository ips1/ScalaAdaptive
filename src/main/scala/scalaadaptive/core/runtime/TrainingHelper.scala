package scalaadaptive.core.runtime

import scalaadaptive.core.options.Selection.Selection

// TODO: Delete this class
/**
  * Created by pk250187 on 5/7/17.
  */
class TrainingHelper(val runner: OptionRunner) {
  def train[TReturnType](functions: Seq[AppliedFunction[TReturnType]],
                         inputDescriptor: Option[Long],
                         selection: Selection): Unit = ??? /* {
    functions.foreach { f =>
      runner.runOption(List(f), inputDescriptor, None, selection)
    }
  } */
}
