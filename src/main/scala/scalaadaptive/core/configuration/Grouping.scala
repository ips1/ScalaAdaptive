package scalaadaptive.core.configuration

/**
  * Created by pk250187 on 3/21/17.
  */
object Grouping extends Enumeration {
  // TODO: What with this?
  type Grouping = Value
  val None = Value("None")
  val Direct = Value("Direct")
  val OrderOfMagnitude = Value("OrderOfMagnitude")
  val Hundreds = Value("Hundreds")
  val Thousands = Value("Hundreds")
}