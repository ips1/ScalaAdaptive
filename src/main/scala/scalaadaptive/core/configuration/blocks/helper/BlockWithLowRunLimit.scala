package scalaadaptive.core.configuration.blocks.helper

/**
  * Created by Petr Kubat on 7/8/17.
  *
  * A base for block with lowRunLimit argument.
  *
  */
trait BlockWithLowRunLimit {
  protected val lowRunLimit = 30
}
