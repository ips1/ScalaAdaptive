package runtime.selection

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator
import runtime.history.RunHistory

/**
  * Created by pk250187 on 4/23/17.
  */
class InterpolationSelector[TMeasurement](implicit num: Numeric[TMeasurement])
  extends RunSelector[TMeasurement] {
  override def selectOption(records: Seq[RunHistory[TMeasurement]], inputDescriptor: Long): RunHistory[TMeasurement] = {
    val interpolator = new LinearInterpolator()
    val polynomials =
      records.map(runHistory => {
        val sortedData = runHistory.runItems
          .groupBy(i => i.inputDescriptor)
          .mapValues(v => num.toDouble(v.map(_.measurement).sum) / v.size)
          .toList
          .sortBy(i => i._1)
        (runHistory,
         interpolator.interpolate(
           sortedData.map(i => i._1.toDouble).toArray,
           sortedData.map(i => i._2).toArray
         )
        )
      })

    val min = polynomials.minBy(p => if (!p._2.isValidPoint(inputDescriptor)) {
      println("Invalid point!")
      -1
    } else p._2.value(inputDescriptor))

    min._1
  }
}
