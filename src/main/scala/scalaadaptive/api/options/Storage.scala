package scalaadaptive.api.options

/**
  * Created by Petr Kubat on 4/22/17.
  */
object Storage extends Enumeration {
  type Storage = Value
  val Local = Value("Local")
  val Global = Value("Global")
  val Persistent = Value("Persistent")
}