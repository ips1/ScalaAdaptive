package scalaadaptive.core.runtime.selection.support

/**
  * Created by pk250187 on 6/8/17.
  */
class AverageForSampleCountSelector(val averageSamplesPerWindow: Int) extends WindowSizeSelector {
  override def selectWindowSize(orderedInputDescriptors: Seq[Long]): Int = {
    val distances =
      orderedInputDescriptors.dropRight(1)
        .zip(orderedInputDescriptors.tail)
        .map(x => x._2 - x._1)
    val averageDistance = distances.sum.toDouble / distances.length

    (averageDistance * averageSamplesPerWindow).toInt
  }
}
