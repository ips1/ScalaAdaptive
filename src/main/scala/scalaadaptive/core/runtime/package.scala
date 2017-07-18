package scalaadaptive.core

/**
  * Created by Petr Kubat on 7/19/17.
  *
  * Package containing all the main runtime components of the ScalaAdaptive framework that exist individually in the
  * system and are used to invoke adaptive functions.
  *
  * The root type is the [[scalaadaptive.core.runtime.AdaptiveCore]], which holds all the other
  * implementations, and is represented at runtime by a singleton [[scalaadaptive.core.runtime.AdaptiveInternal]].
  *
  * The other main components are [[scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker]], which handles the
  * fast invocation process and deals with policies, and the [[scalaadaptive.core.runtime.selection.AdaptiveSelector]],
  * that performs the selection process using various [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]].
  *
  */
package object runtime {

}
