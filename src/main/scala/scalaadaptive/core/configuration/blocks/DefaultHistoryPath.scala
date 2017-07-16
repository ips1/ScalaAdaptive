package scalaadaptive.core.configuration.blocks

import java.nio.file.{Path, Paths}

import scalaadaptive.core.configuration.BaseLongConfiguration

/**
  * Created by Petr Kubat on 5/1/17.
  */
trait DefaultHistoryPath extends BaseLongConfiguration {
  override val rootPath: Path = Paths.get("./adaptive_history")
}
