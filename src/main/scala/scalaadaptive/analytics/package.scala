package scalaadaptive

import scalaadaptive.core.configuration.blocks.analytics.NoAnalyticsCollection

/**
  * Created by Petr Kubat on 7/16/17.
  *
  * A package that contains the tools and types to work with extracted analytical information.
  *
  * The most important type is the [[scalaadaptive.analytics.AnalyticsRunRecord]], which contains observations of
  * one run of the combined function. The records are made accessible using [[scalaadaptive.analytics.AnalyticsData]]
  * type, which can be retrieved from the [[scalaadaptive.api.functions.AdaptiveFunction0]] and corresponding combined
  * function traits.
  *
  * The analytics collection is done by the type [[scalaadaptive.core.functions.analytics.AnalyticsCollector]], which
  * is part of the [[scalaadaptive.core.configuration.Configuration]]. Beware that using the
  * [[scalaadaptive.core.configuration.blocks.analytics.NoAnalyticsCollection]] configuration block will replace
  * the collector with an empty collector and no analytics data will be collected.
  *
  * === Usage example ===
  *
  * {{{
  *   val analytics = testFunction.getAnalyticsData.get
  *   analytics.saveData(new File("analytics.txt"))
  *   analytics.getAllRunInfo.foreach(r => {
  *     if (r.overheadPercentage > 0.5) logRun(r)
  *   })
  * }}}
  *
  */
package object analytics {

}
