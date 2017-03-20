package runtime

import references.FunctionReference

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/19/17.
  */
class RunRecord[TRunItem](val reference: FunctionReference, val buckets: mutable.HashMap[Int, Bucket[TRunItem]]) {
  def getBucket(bucketId: Int): Bucket[TRunItem] =
    buckets.getOrElseUpdate(bucketId, { new Bucket[TRunItem](reference, new ArrayBuffer[TRunItem]()) })
}
