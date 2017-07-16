package scalaadaptive.api.policies

/**
  * Created by Petr Kubat on 5/21/17.
  *
  * Enumeration containing possible results of [[Policy]] decisions.
  *
  */
object PolicyResult extends Enumeration {
  type PolicyResult = Value
  val UseLast = Value("UseLast")
  val UseMost = Value("UseMost")
  val SelectNew = Value("SelectNew")
  val GatherData = Value("GatherData")
}
