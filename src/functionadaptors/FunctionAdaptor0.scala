package functionadaptors

import options.Measurement.Measurement
import options.{Measurement, RunOption}
import runtime.{Adaptive, ReferencedFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor0[R](private val options: List[RunOption[() => R]],
                             private val using: Measurement = Measurement.RunTime,
                             private val bySelector: () => Int = null) extends (() => R) {

  def or(fun: Function0[R]): Function0[R] = or(Implicits.toAdaptor0(fun))
  def or(fun: FunctionAdaptor0[R]): Function0[R] = new FunctionAdaptor0[R](this.options ++ fun.options)
  def by(selector: () => Int): Function0[R] = new FunctionAdaptor0[R](options, using, selector)
  def using(measurement: Measurement): Function0[R] = new FunctionAdaptor0[R](options, measurement, bySelector)

  override def apply(): R = {
    val test = options.map(f => new ReferencedFunction[R]({ () => f.function() }, f.reference))
    Adaptive.tracker.runOption(test)
  }
}