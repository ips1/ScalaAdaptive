package performance

/**
  * Created by pk250187 on 3/19/17.
  */
trait PerformanceProvider[TPerformanceItem] {
  def measureFunctionRun[TOut](fun: () => TOut) : (TOut, TPerformanceItem)
}
