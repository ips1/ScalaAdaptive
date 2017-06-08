package scalaadaptive.math

import scalaadaptive.math.TwoSampleTestResult.Value

/**
  * Created by pk250187 on 6/7/17.
  */
object RegressionTTestResult extends Enumeration {
  type RegressionTTestResult = Value
  val RelationshipExists = Value("RelationshipExists")
  val CantRejectNoRelationship = Value("CantRejectNoRelationship")
}