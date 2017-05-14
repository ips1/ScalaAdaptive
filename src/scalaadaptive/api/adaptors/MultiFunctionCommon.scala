package scalaadaptive.api.adaptors

/**
  * Created by pk250187 on 5/14/17.
  */
trait MultiFunctionCommon {
  def toDebugString: String
  def flushHistory(): Unit
}
