package scalaadaptive.api.options

/**
  * Created by pk250187 on 5/8/17.
  */
object Selection extends Enumeration {
  type Selection = Value
  val MeanBased = Value("MeanBased")
  val InputBased = Value("InputBased")
}