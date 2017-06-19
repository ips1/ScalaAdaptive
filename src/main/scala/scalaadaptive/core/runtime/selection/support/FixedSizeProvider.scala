package scalaadaptive.core.runtime.selection.support

/**
  * Created by pk250187 on 6/8/17.
  */
class FixedSizeProvider(val size: Int) extends WindowSizeProvider {
  override def selectWindowSize(orderedInputDescriptors: Seq[Long]): Int = size
}
