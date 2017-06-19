package scalaadaptive.api.grouping

/**
  * Created by pk250187 on 3/21/17.
  */
abstract class GroupId

case class Group(id: Int) extends GroupId
case class NoGroup() extends GroupId