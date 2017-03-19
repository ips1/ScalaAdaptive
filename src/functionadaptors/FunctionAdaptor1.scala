package functionadaptors

import options.Measurement.Measurement
import options.{Measurement, RunOption}
import runtime.{Adaptive, ReferencedFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor1[I, R](private val options: List[RunOption[(I) => R]],
                             private val using: Measurement = Measurement.RunTime,
                             private val bySelector: (I) => Int = null) extends ((I) => R) {

  def or(fun: (I) => R): Function1[I, R] = or(Implicits.toAdaptor1(fun))
  def or(fun: FunctionAdaptor1[I, R]): Function1[I, R] = new FunctionAdaptor1[I, R](this.options ++ fun.options)
  def by(selector: (I) => Int): Function1[I, R] = new FunctionAdaptor1[I, R](options, using, selector)
  def using(measurement: Measurement): Function1[I, R] = new FunctionAdaptor1[I, R](options, measurement, bySelector)

  override def apply(v1: I): R = {
    val test = options.map(f => new ReferencedFunction[R]({ () => f.function(v1) }, f.reference))
    Adaptive.tracker.runOption(test)
  }
}
