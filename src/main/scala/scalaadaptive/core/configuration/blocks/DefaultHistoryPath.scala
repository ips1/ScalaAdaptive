package scalaadaptive.core.configuration.blocks

import java.nio.file.{Path, Paths}

import scalaadaptive.core.configuration.BaseLongConfiguration

/**
  * Created by Petr Kubat on 5/1/17.
  *
  * Block setting the default path to persistent history - "./adaptive_history" (in the executing application folder).
  *
  */
trait DefaultHistoryPath extends BaseLongConfiguration {
  override val rootPath: Path = Paths.get("./adaptive_history")
}
