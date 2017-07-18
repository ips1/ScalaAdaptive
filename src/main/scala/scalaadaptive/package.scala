/**
  * Created by Petr Kubat on 7/18/17.
  *
  * =ScalaAdaptive=
  *
  * ScalaAdaptive is a simple Scala framework that allows development using adaptive functions, i.e. functions with
  * multiple implementations that adaptively decide which one to use upon every invocation.
  *
  * The main advantages of the framework are simplicity and transparency - it can be incorporated into an existing
  * project without requiring any major changes.
  *
  * As to the implementation, the framework internals are highly modular and extensible. The core types are:
  *  - [[scalaadaptive.core.runtime.AdaptiveCore]] - singleton providing runtime access to module implementations
  *  - [[scalaadaptive.core.functions.CombinedFunction]] - a type for function with multiple implementation
  *  - [[scalaadaptive.core.runtime.invocation.CombinedFunctionInvoker]] - handles invocation of combined functions
  *  - [[scalaadaptive.core.runtime.selection.AdaptiveSelector]] - handles selection between multiple functions
  *  - [[scalaadaptive.core.runtime.history.historystorage.HistoryStorage]] - stores historical measurement records of the functions
  *  - [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]] - a strategy to select the function
  *
  * The actual framework module composition is defined statically using the
  * [[scalaadaptive.core.configuration.Configuration]] type. For more information see package
  * [[scalaadaptive.core.configuration]].
  *
  * The API, consisting mainly of the types from the [[scalaadaptive.api]] package, is accessible via
  * [[scalaadaptive.api.Implicits]] objects and its conversion macros. After using the conversion, the user interacts
  * with the framework via a [[scalaadaptive.api.functions.AdaptiveFunction0]] type (or a corresponding one for
  * more arguments).
  *
  */
package object scalaadaptive {

}
