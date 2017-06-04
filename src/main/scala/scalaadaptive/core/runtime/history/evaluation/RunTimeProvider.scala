package scalaadaptive.core.runtime.history.evaluation

/**
  * Created by pk250187 on 3/19/17.
  */
class RunTimeProvider extends EvaluationProvider[Long] {
  override def evaluateFunctionRun[TArgType, TReturnType](fun: (TArgType) => TReturnType,
                                                          args: TArgType): (TReturnType, Long) = {
    val startTime = System.nanoTime()
    val result = fun(args)
    val elapsed = System.nanoTime() - startTime
    (result, elapsed)
  }
}
