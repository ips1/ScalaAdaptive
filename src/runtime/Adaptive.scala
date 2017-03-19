package runtime

import performance.RunTimeProvider

/**
  * Created by pk250187 on 3/19/17.
  */
object Adaptive {
  val tracker = new RunTracker(new SimpleOptionSelector, new RunTimeProvider)
}
