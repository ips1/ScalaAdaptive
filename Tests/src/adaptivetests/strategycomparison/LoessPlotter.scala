package adaptivetests.strategycomparison

import org.apache.commons.math3.analysis.interpolation.LoessInterpolator

/**
  * Created by pk250187 on 7/9/17.
  */
object LoessPlotter {
  def main(args: Array[String]): Unit = {
    val data = List((188997,5689889),
    (221282,7875009),
    (204815,5350410),
    (160322,7972012),
    (147721,4721678),
    (429830,87248894),
    (109600,5457237),
    (225128,6243919),
    (476342,14835151),
    (132161,4698172),
    (272377,10970118),
    (484391,19719746),
    (226129,7004949),
    (462128,18524277),
    (414579,13758086),
    (313173,11260733),
    (169619,7216593),
    (380402,16427247),
    (251830,8269244),
    (212586,8060789),
    (324773,11660188),
    (230760,8213950),
    (127997,3205365),
    (338880,93427185),
    (445540,12825389),
    (433523,13803620),
    (142550,3924343),
    (304209,11246704),
    (127901,3197577),
    (450489,16173612))

    val sortedData = data.sortBy(_._1)

    val xList = sortedData.map(_._1)
    val yList = sortedData.map(_._2)

    val xMin = xList.min
    val xMax = xList.max

    val interval = 1000

    val xArray = xList.map(_.toDouble).toArray
    val yArray = yList.map(v => v.toDouble).toArray

    val interpolator = new LoessInterpolator(1.0, 2)
    val interpolation = interpolator.interpolate(xArray, yArray)

    Seq.range(xMin, xMax, interval).foreach(i => {
      if (interpolation.isValidPoint(i)) {
        val point = interpolation.value(i)
        println(s"$i,$point")
      }
    })
  }
}
