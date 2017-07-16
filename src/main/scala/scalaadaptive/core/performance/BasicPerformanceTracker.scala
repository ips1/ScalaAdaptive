package scalaadaptive.core.performance

/**
  * Created by Petr Kubat on 5/1/17.
  */
class BasicPerformanceTracker extends PerformanceTracker {
  private var selectionTimeTotal: Long = 0
  private var storingTimeTotal: Long = 0
  private var functionTimeTotal: Long = 0

  private var trackingFrom: Option[Long] = None

  override def addStoringTime(time: Long): Unit = storingTimeTotal += time

  override def addSelectionTime(time: Long): Unit = selectionTimeTotal += time

  override def addFunctionTime(time: Long): Unit = functionTimeTotal += time

  override def getPerformance = new PerformanceData(functionTimeTotal, selectionTimeTotal, storingTimeTotal)

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

  override def reset(): Unit = {
    selectionTimeTotal = 0
    storingTimeTotal = 0
    functionTimeTotal = 0
    trackingFrom = None
  }
}
