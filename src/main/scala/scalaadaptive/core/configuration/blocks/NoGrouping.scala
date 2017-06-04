package scalaadaptive.core.configuration.blocks

import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.runtime.grouping.{GroupSelector, SingleGroupSelector}

/**
  * Created by pk250187 on 5/1/17.
  */
trait NoGrouping extends Configuration {
  override val groupSelector: GroupSelector = new SingleGroupSelector()
}
