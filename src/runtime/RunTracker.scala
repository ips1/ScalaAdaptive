package runtime

import performance.PerformanceProvider
import references.FunctionReference

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

import options.RunOption

/**
  * Created by pk250187 on 3/19/17.
  */
class RunTracker(runSelector: OptionSelector[Long],
                 performanceProvider: PerformanceProvider[Long]) {
  private val records: mutable.HashMap[FunctionReference, RunRecord[Long]] =
    new mutable.HashMap[FunctionReference, RunRecord[Long]]()

  def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]]): TReturnType = {
    val optionRecords = options.map(f => records.getOrElseUpdate(f.reference, new RunRecord[Long](f.reference, new ArrayBuffer[Long]())))
    val selectedRecord = runSelector.selectOption(optionRecords)
    val (result, performance) = performanceProvider.measureFunctionRun(options.find(_.reference == selectedRecord.reference).get.fun)
    selectedRecord.applyNewRun(performance)
    return result
  }
}
