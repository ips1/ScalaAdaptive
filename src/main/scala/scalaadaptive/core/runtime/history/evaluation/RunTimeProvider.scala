package scalaadaptive.core.runtime.history.evaluation

/**
  * Created by Petr Kubat on 3/19/17.
  *
  * An [[EvaluationProvider]] that measures the wall-clock time of the function execution and returns it as the
  * evaluation.
  *
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
