package scalaadaptive.api.functions

import scala.language.experimental.macros
import scalaadaptive.core.macros.OrMacroImpl
import scalaadaptive.api.grouping.Group

/**
  * Created by Petr Kubat on 5/27/17.
  *
  * A type representing an adaptive function with 0 arguments and a return value. Can be used anywhere a normal
  * [[Function0]] could be used. Contains a set of implementations that are of the same argument arity and types.
  *
  * Upon invocation, this adaptive function will perform a chain of selection and will execute one of the inner
  * representations depending on the historical measurements and input. It might also measure the run time and
  * store the run information into the run history.
  *
  */
trait AdaptiveFunction0[R] extends AdaptiveFunctionCommon[Unit, R, AdaptiveFunction0[R]] with Function0[R] {
  /**
    * Combines this function with another function, i.e. adds a new implementation option. Note that the other function
    * can be an instance of [[AdaptiveFunction0]] as well, in such a case, it combines all implementations from both
    * adaptive functions.
    *
    * Beware that it is implemented by a compile time macro and thus will be executed at compile time.
    * @param fun The other function to combine it with.
    * @return A completely new instance of [[AdaptiveFunction0]] containing all the implementations.
    */
  def or(fun: () => R): AdaptiveFunction0[R] = macro OrMacroImpl.or_impl[() => R, AdaptiveFunction0[R]]

  /**
    * Sets an input descriptor function (selector) for this adaptive function. This selector will be executed each time
    * upon invocation on the input of the function and should return an input descriptor, a Long that describes the input
    * with regard to expected run time, i.e. size of the input.
    * @param selector The function that generates input descriptor from the input.
    * @return A completely new instance of [[AdaptiveFunction0]] containing the input descriptor function.
    */
  def by(selector: () => Long): AdaptiveFunction0[R]

  /**
    * Sets a group selector for this adaptive function. This selector will be executed each time upon invocation on the
    * input and should return a group to which the input belongs. The historical records will always have effect only
    * on the inputs from the same group. The group selection should reflect some non-measurable factors of the input,
    * i.e. if it is sorted, if caching is enabled, etc., and can also divide inputs into smaller groups by e.g. orders
    * of magnitude of their sizes.
    * @param selector The function that generates the group from the input.
    * @return A completely new instance of [[AdaptiveFunction0]] containing the group selector.
    */
  def groupBy(selector: () => Group): AdaptiveFunction0[R]

  /**
    * Invokes the adaptive function (with the entire selection process), but delays the measurement using an
    * [[InvocationToken]].
    * @return The return value of the executed implementation.
    */
  def applyWithoutMeasuring(): (R, InvocationToken)

  /**
    * A syntactic shortcut for the [[applyWithoutMeasuring]] method.
    */
  def ^(): (R, InvocationToken) = applyWithoutMeasuring()

  /**
    * Combined this function directly with another instance of adaptive function. For more information see [[or]]
    * @param fun The other adaptive function to combine it with.
    * @return A completely new instance of [[AdaptiveFunction0]] containing all the implementations.
    */
  def orMultiFunction(fun: AdaptiveFunction0[R]): AdaptiveFunction0[R]
}
