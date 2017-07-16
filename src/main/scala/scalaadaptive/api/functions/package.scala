package scalaadaptive.api

/**
  * Created by Petr Kubat on 7/16/17.
  *
  * Package containing the key types of the framework - types representing the combined functions, i.e. functions with
  * multiple different implementations that are selected adaptively.
  *
  * Whenever user of the framework combines two function using the {{{or}}} method, he receives an instance of
  * [[scalaadaptive.api.functions.AdaptiveFunction0]] or a corresponding type with more arguments. Currently, types
  * for functions up to 5 arguments are implemented in the framework.
  *
  */
package object functions {

}
