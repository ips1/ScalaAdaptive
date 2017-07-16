package scalaadaptive.api.functions

/**
  * Created by Petr Kubat on 4/23/17.
  *
  * A token that serves for the delayed measurement of a function that was influenced by a previous selection.
  * Internally holds an information about previously selected function.
  *
  * The lifecycle is following:
  * - a combined function is created ([[AdaptiveFunction0]] or corresponding types)
  * - a multi function is invoked using the special {{{^()}}} operator - one implementation is selected and the result
  * is returned along with the [[InvocationToken]] instance
  * - user repeatedly invokes the function that was affected by the decision and should be measured using the {{{()}}}
  * operator on the [[InvocationToken]]
  *
  * A usage example:
  * {{{
  *   val (result, token) = multiFunction^(input)
  *   ...
  *   val out = token(() => doStuff(result, a, b, c))
  * }}}
  *
  */
trait InvocationToken {
  /**
    * A shortcut apply operator for the [[runMeasuredFunction]].
    */
  def apply[TReturnValue](fun: () => TReturnValue): TReturnValue = runMeasuredFunction(fun)

  /**
    * Executes given function, evaluates its run and stores the evaluation data to the [[scalaadaptive.core.runtime.history.runhistory.RunHistory]]
    * of previously selected function.
    * @param fun The function to run and evaluate
    * @tparam TReturnValue The return value type of the function
    * @return The result of the function
    */
  def runMeasuredFunction[TReturnValue](fun: () => TReturnValue): TReturnValue
}
