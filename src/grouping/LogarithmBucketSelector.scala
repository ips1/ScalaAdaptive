package grouping

/**
  * Created by pk250187 on 3/20/17.
  */
class LogarithmBucketSelector extends BucketSelector {
  override def selectBucketForValue(value: Int): Int = Math.log10(value).toInt
}
