package scalaadaptive.core.runtime.performance

/**
  * Created by Petr Kubat on 6/6/17.
  *
  * Simple implementation of [[PerformanceProvider]].
  *
  */
class PerformanceData(val functionTime: Long,
                      val selectionTime: Long,
                      val storingTime: Long) extends PerformanceProvider {
  override def getFunctionTime: Long = functionTime
  override def getOverheadTime: Long = selectionTime + storingTime
  override def getOverheadPercentage: Double = if (functionTime > 0) getOverheadTime.toDouble / functionTime else 0

  override def toLogString: String =
    s"Selection time: $selectionTime, storing time: $storingTime \ntotal " +
    s"overhead time: $getOverheadTime, " +
    s"function time: $functionTime, overhead percentage: ${getOverheadPercentage * 100}%"
}
