package grouping

/**
  * Created by pk250187 on 3/20/17.
  */
class LogarithmBucketSelector extends BucketSelector {
  override def selectBucketForValue(value: Int): BucketId = new BucketId(Math.log10(value).toInt)
}
