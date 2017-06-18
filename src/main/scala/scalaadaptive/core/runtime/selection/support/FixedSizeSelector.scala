package scalaadaptive.core.runtime.selection.support

/**
  * Created by pk250187 on 6/8/17.
  */
class FixedSizeSelector(val size: Int) extends WindowSizeSelector {
  override def selectWindowSize(orderedInputDescriptors: Seq[Long]): Int = size
}
