package scalaadaptive.api.adaptors

import scalaadaptive.api.policies.Policy

/**
  * Created by Petr Kubat on 6/28/17.
  */
trait MultiFunctionControl {
  def toDebugString: String
  def flushHistory(): Unit
  def setPolicy(policy: Policy): Unit
  def resetPolicy(): Unit
}
