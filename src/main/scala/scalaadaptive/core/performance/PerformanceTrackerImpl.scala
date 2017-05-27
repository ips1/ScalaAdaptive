package scalaadaptive.core.performance

/**
  * Created by pk250187 on 5/1/17.
  */
class PerformanceTrackerImpl extends PerformanceTracker {
  private var selectionTimeTotal: Long = 0
  private var storingTimeTotal: Long = 0
  private var functionTimeTotal: Long = 0

  private var trackingFrom: Option[Long] = None

  override def addStoringTime(time: Long): Unit = storingTimeTotal += time

  override def addSelectionTime(time: Long): Unit = selectionTimeTotal += time

  override def addFunctionTime(time: Long): Unit = functionTimeTotal += time

  override def getOverheadTime: Long = selectionTimeTotal + storingTimeTotal

  override def getFunctionTime: Long = functionTimeTotal

  override def getOverheadPercentage: Double = if (functionTimeTotal > 0) getOverheadTime.toDouble / functionTimeTotal else 0

  override def getStatistics: String =
    s"Selection time: $selectionTimeTotal, storing time: $storingTimeTotal \ntotal overhead time: $getOverheadTime, function time: $functionTimeTotal, overhead percentage: ${getOverheadPercentage * 100}%"

  override def addStoringTime(): Unit = {
    trackingFrom.foreach(t => addStoringTime(System.nanoTime() - t))
    startTracking()
  }

  override def addSelectionTime(): Unit = {
    trackingFrom.foreach(t => addSelectionTime(System.nanoTime() - t))
    startTracking()
  }

  override def addFunctionTime(): Unit = {
    trackingFrom.foreach(t => addFunctionTime(System.nanoTime() - t))
    startTracking()
  }

  override def startTracking(): Unit = {
    trackingFrom = Some(System.nanoTime())
  }
}
