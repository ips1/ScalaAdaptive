package adaptivetests.delayed

/**
  * Created by pk250187 on 4/23/17.
  */
abstract class DelayedConfig

case class SlowConfig() extends DelayedConfig
case class FastConfig() extends DelayedConfig
