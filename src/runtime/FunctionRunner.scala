package runtime

import runtime.history.HistoryKey

/**
  * Created by pk250187 on 3/26/17.
  */
trait FunctionRunner {
  def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]], inputDescriptor: Long = 0): TReturnType
  def runOptionWithDelayedMeasure[TReturnType](options: Seq[ReferencedFunction[TReturnType]],
                                               inputDescriptor: Long = 0): (TReturnType, MeasurementToken)
  def runMeasuredFunction[TReturnType](fun: () => TReturnType, key: HistoryKey, inputDescriptor: Long): TReturnType
}
