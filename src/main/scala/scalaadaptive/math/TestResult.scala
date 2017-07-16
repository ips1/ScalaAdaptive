package scalaadaptive.math

/**
  * Created by Petr Kubat on 5/2/17.
  *
  * A result of a statistical test with two sided alternative by default.
  *
  */
object TestResult extends Enumeration {
  type TestResult = Value
  val CantRejectEquality = Value("CantRejectEquality")
  val ExpectedLower = Value("ExpectedLower")
  val ExpectedHigher = Value("ExpectedHigher")
}