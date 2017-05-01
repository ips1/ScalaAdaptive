package scalaadaptive.core.runtime.history

/**
  * Created by pk250187 on 5/1/17.
  */
class GroupedRunData[TMeasurement](val averageRunData: RunData[TMeasurement], val runCount: Int)
