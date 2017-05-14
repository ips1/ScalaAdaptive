package scalaadaptive.math

/**
  * Created by pk250187 on 5/2/17.
  */
abstract class TestResult

case class CantRejectEquality() extends TestResult
case class LowerExpectation() extends TestResult
case class HigherExpectation() extends TestResult

