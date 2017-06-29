package scalaadaptive.core.runtime.selection.support

/**
  * Created by pk250187 on 6/8/17.
  */
trait WindowSizeProvider {
  def selectWindowSize(orderedInputDescriptors: Seq[Long]): Int
}