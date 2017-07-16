package scalaadaptive.api.grouping

/**
  * Created by Petr Kubat on 3/21/17.
  *
  * Group specifier for a function input.
  *
  */
abstract class Group

/** A concrete group with given id */
case class GroupId(id: Int) extends Group
/** A rest group with no ID */
case class NoGroup() extends Group