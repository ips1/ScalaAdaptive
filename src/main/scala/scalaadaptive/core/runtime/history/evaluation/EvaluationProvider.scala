package scalaadaptive.core.runtime.history.evaluation

/**
  * Created by Petr Kubat on 3/19/17.
  */
trait EvaluationProvider[TMeasurement] {
  def evaluateFunctionRun[TArgType, TReturnType](fun: (TArgType) => TReturnType,
                                                 args: TArgType): (TReturnType, TMeasurement)
}
