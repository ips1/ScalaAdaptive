package options

import references.FunctionReference

/**
  * Created by pk250187 on 3/19/17.
  */
class RunOption[TFunction](val function: TFunction, val reference: FunctionReference)
//
//object RunOption {
//  // Factory method for better type inference
//  def create[T](function: T, reference: FunctionReference) = new RunOption[T](function, reference)
//}
