package scalaadaptive.math

/**
  * Created by pk250187 on 5/2/17.
  */
object TwoSampleTestResult extends Enumeration {
  type TwoSampleTestResult = Value
  val CantRejectEquality = Value("CantRejectEquality")
  val LowerExpectation = Value("LowerExpectation")
  val HigherExpectation = Value("HigherExpectation")
}