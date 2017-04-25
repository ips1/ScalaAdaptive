package scalaadaptive.core.configuration

/**
  * Created by pk250187 on 3/21/17.
  */
object Grouping extends Enumeration {
  type Grouping = Value
  val None = Value("None")
  val OrderOfMagnitude = Value("OrderOfMagnitude")
  val Hundreds = Value("Hundreds")
  val Thousands = Value("Hundreds")
}