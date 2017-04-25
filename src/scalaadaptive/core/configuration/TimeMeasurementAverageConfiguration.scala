package scalaadaptive.core.configuration

import java.nio.file.{Path, Paths}

import scalaadaptive.core.grouping.{GroupSelector, SingleGroupSelector}
import scalaadaptive.core.performance.{PerformanceProvider, RunTimeProvider}
import scalaadaptive.core.runtime.selection.{BestAverageSelector, InterpolationSelector, LowRunAwareSelector, RunSelector}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by pk250187 on 3/26/17.
  */
object TimeMeasurementAverageConfiguration extends BaseLongConfiguration {
  override val rootPath: Path = Paths.get("./adaptive_history")
  //override val runSelector: RunSelector[Long] = new LowRunAwareSelector[Long](new BestAverageSelector(), 20)
  override val runSelector: RunSelector[Long] = new LowRunAwareSelector[Long](new InterpolationSelector[Long](), 30)
  override val performanceProvider: PerformanceProvider[Long] = new RunTimeProvider

  // Temporary change:
  override val groupSelector: GroupSelector = new SingleGroupSelector()
}
