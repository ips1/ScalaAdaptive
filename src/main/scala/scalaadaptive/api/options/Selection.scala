package scalaadaptive.api.options

/**
  * Created by Petr Kubat on 5/8/17.
  *
  * Enumeration with selection strategy options.
  *
  */
object Selection extends Enumeration {
  type Selection = Value
  val MeanBased = Value("MeanBased")
  val InputBased = Value("InputBased")
}