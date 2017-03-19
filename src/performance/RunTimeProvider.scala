package performance

/**
  * Created by pk250187 on 3/19/17.
  */
class RunTimeProvider extends PerformanceProvider[Long] {
  override def measureFunctionRun[TOut](fun: () => TOut): (TOut, Long) = {
    val startTime = System.nanoTime()
    val result = fun()
    val elapsed = System.nanoTime() - startTime
    (result, elapsed)
  }
}
