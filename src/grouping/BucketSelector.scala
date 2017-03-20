package grouping

/**
  * Created by pk250187 on 3/20/17.
  */
trait BucketSelector {
  def selectBucketForValue(value: Int): Int
}
