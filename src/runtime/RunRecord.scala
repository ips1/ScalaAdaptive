package runtime

import references.FunctionReference

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/19/17.
  */
class RunRecord[TRunItem](val reference: FunctionReference, val runItems: ArrayBuffer[TRunItem]) {
  def runCount: Int = runItems.size
  def applyNewRun(runResult: TRunItem) = runItems.append(runResult)
}
