package scalaadaptive.core.options

/**
  * Created by pk250187 on 5/8/17.
  */
object Selection extends Enumeration {
  type Selection = Value
  val Discrete = Value("Discrete")
  val Continuous = Value("Continuous")
}