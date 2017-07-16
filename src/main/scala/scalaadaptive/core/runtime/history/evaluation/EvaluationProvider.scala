package scalaadaptive.core.runtime.history.evaluation

/**
  * Created by Petr Kubat on 3/19/17.
  *
  * An evaluation provider that can invoke a function and evaluate its run.
  *
  */
trait EvaluationProvider[TMeasurement] {
  /**
    * Invokes the function and evaluates its run.
    * @param fun The function to be invoked.
    * @param args The argument of the function.
    * @tparam TArgType The argument type of the function.
    * @tparam TReturnType The return type of the function.
    * @return The return value of the function along with the measurement.
    */
  def evaluateFunctionRun[TArgType, TReturnType](fun: (TArgType) => TReturnType,
                                                 args: TArgType): (TReturnType, TMeasurement)
}
