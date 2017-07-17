package scalaadaptive.core.configuration.blocks.helper

import java.nio.file.{Path, Paths}

/**
  * Created by Petr Kubat on 7/17/17.
  *
  * A base for blocks with rootHistoryPath argument.
  *
  */
trait BlockWithHistoryPath {
  val rootHistoryPath: Path = Paths.get("./adaptive_history")
}
