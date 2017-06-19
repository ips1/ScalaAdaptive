package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.api.grouping.{GroupSelector, SingleGroupSelector}

/**
  * Created by pk250187 on 5/1/17.
  */
trait NoGrouping extends Configuration {
  override val createGroupSelector: () => GroupSelector =
    () => new SingleGroupSelector()
}
