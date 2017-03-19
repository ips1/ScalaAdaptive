package functionadaptors

import options.Measurement.Measurement
import options.{Measurement, RunOption}
import references.FunctionReference

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor1[I, R](private val options: List[RunOption[(I) => R]],
                             private val using: Measurement = Measurement.RunTime,
                             private val bySelector: (I) => Int = null) extends ((I) => R) {

  def or(fun: FunctionAdaptor1[I, R]) = new FunctionAdaptor1[I, R](this.options ++ fun.options)
  def by(selector: (I) => Int) = new FunctionAdaptor1[I, R](options, using, selector)
  def using(measurement: Measurement) = new FunctionAdaptor1[I, R](options, measurement, bySelector)

  override def apply(v1: I): R = ???
}
