package scalaadaptive.math

/**
  * Created by pk250187 on 5/2/17.
  */
object TTestResult extends Enumeration {
  type TTestResult = Value
  val CantRejectEquality = Value("CantRejectEquality")
  val LowerMean = Value("LowerMean")
  val HigherMean = Value("HigherMean")
}