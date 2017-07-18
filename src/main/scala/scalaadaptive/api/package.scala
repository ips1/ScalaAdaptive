package scalaadaptive

/**
  * Created by Petr Kubat on 7/19/17.
  *
  * Package containing all the types that the user of the framework will need to work with. The most important
  * object is the [[scalaadaptive.api.Implicits]], containing macros for conversion from normal function types to
  * [[scalaadaptive.api.functions.AdaptiveFunction0]] and corresponding types, which then allow the user to further
  * interact with the framework.
  *
  * The [[scalaadaptive.api.Adaptive]] object allows the user to initialize the whole framework.
  *
  * The [[scalaadaptive.api.policies]] package contains a variety of policies for the user to apply to the adaptive
  * function, as well as a DSL to build his own (see [[scalaadaptive.api.policies.builder]]).
  *
  */
package object api {

}
