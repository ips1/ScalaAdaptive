package scalaadaptive.core.configuration.blocks

import java.nio.file.{Path, Paths}

import scalaadaptive.core.configuration.BaseLongConfiguration

/**
  * Created by pk250187 on 5/1/17.
  */
trait DefaultPath extends BaseLongConfiguration {
  override val rootPath: Path = Paths.get("./adaptive_history")
}
