package scalaadaptive.api.grouping

/**
  * Created by Petr Kubat on 3/21/17.
  */
abstract class Group

case class GroupId(id: Int) extends Group
case class NoGroup() extends Group