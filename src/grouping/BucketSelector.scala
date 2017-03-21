package grouping

/**
  * Created by pk250187 on 3/20/17.
  */
trait BucketSelector {
  val defaultBucket = new BucketId(0)
  def selectBucketForValue(value: Int): BucketId
}
