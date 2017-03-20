package runtime

import references.FunctionReference

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/20/17.
  */
class Bucket[TRunItem](val reference: FunctionReference, val runItems: ArrayBuffer[TRunItem]) {
  def runCount: Int = runItems.size
  def applyNewRun(runResult: TRunItem): Unit = runItems.append(runResult)
}
