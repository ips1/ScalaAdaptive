package functionadaptors

import options.Measurement.Measurement
import options.{Measurement, RunOption}
import runtime.{Adaptive, ReferencedFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor1[T, R](private val options: List[RunOption[(T) => R]],
                             private val using: Measurement = Measurement.RunTime,
                             private val bySelector: (T) => Int = null) extends ((T) => R) {

  def or[J <: T, S >: R](fun: (J) => S): (J) => S = orAdaptor(Implicits.toAdaptor(fun))
  private def orAdaptor[J <: T, S >: R](fun: FunctionAdaptor1[J, S]): (J) => S =
    new FunctionAdaptor1[J, S](this.options.map(opt => new RunOption[(J) => S](arg => opt.function(arg), opt.reference)) ++ fun.options)
  def by(selector: (T) => Int): (T) => R = new FunctionAdaptor1[T, R](options, using, selector)
  def using(measurement: Measurement): (T) => R = new FunctionAdaptor1[T, R](options, measurement, bySelector)

  override def apply(v1: T): R = {
    val test = options.map(f => new ReferencedFunction[R]({ () => f.function(v1) }, f.reference))
    val by = if (bySelector != null) bySelector(v1) else 0
    Adaptive.tracker.runOption(test, by)
  }
}
