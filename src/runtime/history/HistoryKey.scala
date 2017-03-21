package runtime.history

import grouping.BucketId
import references.FunctionReference

/**
  * Created by pk250187 on 3/21/17.
  */
case class HistoryKey(function: FunctionReference,
                      bucketId: BucketId)
