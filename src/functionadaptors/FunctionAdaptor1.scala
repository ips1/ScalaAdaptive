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

  def or[J <: I, S >: R](fun: (J) => S): Function1[J, S] = orAdaptor(Implicits.toAdaptor1(fun))
  private def orAdaptor[J <: I, S >: R](fun: FunctionAdaptor1[J, S]): (J) => S =
    new FunctionAdaptor1[J, S](this.options.map(opt => new RunOption[(J) => S](arg => opt.function(arg), opt.reference)) ++ fun.options)
  def by(selector: (I) => Int): (I) => R = new FunctionAdaptor1[I, R](options, using, selector)
  def using(measurement: Measurement): (I) => R = new FunctionAdaptor1[I, R](options, measurement, bySelector)

  override def apply(v1: I): R = {
    val test = options.map(f => new ReferencedFunction[R]({ () => f.function(v1) }, f.reference))
    val by = if (bySelector != null) bySelector(v1) else 0
    Adaptive.tracker.runOption(test, by)
  }
}
