package scalaadaptive.math

import scalaadaptive.math.TestResult.Value

/**
  * Created by Petr Kubat on 6/7/17.
  */
object RegressionTTestResult extends Enumeration {
  type RegressionTTestResult = Value
  val RelationshipExists = Value("RelationshipExists")
  val CantRejectNoRelationship = Value("CantRejectNoRelationship")
}