package runtime

import grouping.BucketSelector
import performance.PerformanceProvider
import references.FunctionReference

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import options.RunOption

/**
  * Created by pk250187 on 3/19/17.
  */
class RunTracker(runSelector: OptionSelector[Long],
                 performanceProvider: PerformanceProvider[Long],
                 bucketSelector: BucketSelector) {
  private val records: mutable.HashMap[FunctionReference, RunRecord[Long]] =
    new mutable.HashMap[FunctionReference, RunRecord[Long]]()

  def runOption[TReturnType](options: Seq[ReferencedFunction[TReturnType]], byValue: Int = 0): TReturnType = {
    // TODO: byValue not specified
    val targetBucket = if (byValue != 0) bucketSelector.selectBucketForValue(byValue) else 0
    //println(s"Target bucket: ${targetBucket}, byValue: ${byValue}")

    val optionRecords = options.map(f => records.getOrElseUpdate(f.reference,
      new RunRecord[Long](f.reference, new mutable.HashMap[Int, Bucket[Long]]())))

    val buckets = optionRecords.map(r => r.getBucket(targetBucket))

    val selectedRecord = runSelector.selectOption(buckets)
    val (result, performance) = performanceProvider.measureFunctionRun(options.find(_.reference == selectedRecord.reference).get.fun)
    selectedRecord.applyNewRun(performance)
    return result
  }
}
