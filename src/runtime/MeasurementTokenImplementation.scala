package runtime

import runtime.history.HistoryKey

/**
  * Created by pk250187 on 4/23/17.
  */
class MeasurementTokenImplementation(private val runner: FunctionRunner,
                                     private val inputDescriptor: Long,
                                     private val key: HistoryKey) extends MeasurementToken {
  override def runMeasuredFunction[TReturnValue](fun: () => TReturnValue): TReturnValue =
    runner.runMeasuredFunction(fun, key, inputDescriptor)
}
