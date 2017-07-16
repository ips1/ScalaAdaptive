package scalaadaptive.api.policies

/**
  * Created by Petr Kubat on 5/21/17.
  */
object PolicyResult extends Enumeration {
  type PolicyResult = Value
  // val TurnOff = Value("TurnOff")
  val UseLast = Value("UseLast")
  val UseMost = Value("UseMost")
  val SelectNew = Value("SelectNew")
  val GatherData = Value("GatherData")
}
