package scalaadaptive.core

import scalaadaptive.core.configuration.blocks.storage.CachedStatisticsStorage

/**
  * Created by Petr Kubat on 7/17/17.
  *
  * The configuration determines the actual implementations used within the ScalaAdaptive framework, along with the way
  * how its composed.
  *
  * The [[scalaadaptive.core.configuration.Configuration]] trait provides factory methods for all the important types
  * used in the ScalaAdaptive framework that are hidden behind a trait (in the role of an interface). Upon
  * initialization, the [[scalaadaptive.core.runtime.AdaptiveInternal]] singleton executes these factory methods
  * and composes the framework runtime objects using the results.
  *
  * By extending [[scalaadaptive.core.configuration.Configuration]] and providing custom factory methods, the user can
  * completely change the framework behavior without actually modifying the code. Custom implementation of any of the
  * traits can be used.
  *
  * For some traits (like [[scalaadaptive.core.runtime.selection.strategies.SelectionStrategy]] or
  * [[scalaadaptive.core.runtime.history.runhistory.RunHistory]]) the ScalaAdaptive provides multiple implementations
  * by default and more advanced user of the framework is expected to switch between them. To simplify the process
  * and hide the actual constructor calls, so called configuration blocks were introduced. Blocks are mixin traits
  * implementing only one (or a couple of) factory method of the [[scalaadaptive.core.configuration.Configuration]] in
  * a certain way, and an actual configuration can be composed of these mixins. Note that the mixins might use the
  * parent implementation (see e.g. [[scalaadaptive.core.configuration.blocks.storage.CachedStatisticsStorage]]) and
  * therefore can be stacked together with different blocks - the ordering matters.
  *
  * The [[scalaadaptive.core.configuration.BaseConfiguration]] and
  * [[scalaadaptive.core.configuration.BaseLongConfiguration]] traits were introduced to provide default implementations
  * for types without multiple options in the framework itself (i.e. for the types where the blocks wouldn't make any
  * sense, as there would be a single one).
  *
  * The [[scalaadaptive.core.configuration.defaults.DefaultConfiguration]] represents the configuration that is used
  * for the first initialization, before the user provides his own configuration. It is based on the
  * [[scalaadaptive.core.configuration.BaseLongConfiguration]] and uses mixin blocks to achieve the default behavior.
  *
  * The recommended way of creating configurations is to extend either one of the base configuration traits or the
  * [[scalaadaptive.core.configuration.defaults.DefaultConfiguration]] and add custom configuration blocks (some might
  * even override the ones used in the base trait or class).
  *
  * === Usage example ===
  *
  * {{{
  * val configuration = new DefaultConfiguration
  *   with ConsoleLogging
  *   with NoAnalyticsCollection
  *   with LoessInterpolationInputBasedStrategy
  *   with UTestMeanBasedStrategy
  *   with CachedGroupStorage {
  *   // More detailed arguments and values (either of the original configuration or of the mixins) can be specified
  *   // by overriding the value
  *   override val maximumNumberOfRecords = 1000
  *   override val alpha = 0.1
  * }
  * }}}
  *
  */
package object configuration {

}
