package functionadaptors

import options.Measurement.Measurement
import options.{Measurement, RunOption}
import runtime.{Adaptive, ReferencedFunction}

/**
  * Created by pk250187 on 4/2/17.
  */
class FunctionAdaptor2[I1, I2, R](private val options: List[RunOption[(I1, I2) => R]],
                                  private val using: Measurement = Measurement.RunTime,
                                  private val bySelector: (I1, I2) => Int = null) extends ((I1, I2) => R) {

  def or[J1 <: I1, J2 <: I2, S >: R](fun: (J1, J2) => S): (J1, J2) => S = orAdaptor(Implicits.toAdaptor2(fun))
  private def orAdaptor[J1 <: I1, J2 <: I2, S >: R](fun: FunctionAdaptor2[J1, J2, S]): (J1, J2) => S =
    new FunctionAdaptor2[J1, J2, S](this.options.map(opt => new RunOption[(J1, J2) => S]((a1, a2) => opt.function(a1, a2), opt.reference)) ++ fun.options)
  def by(selector: (I1, I2) => Int): (I1, I2) => R = new FunctionAdaptor2[I1, I2, R](options, using, selector)
  def using(measurement: Measurement): (I1, I2) => R = new FunctionAdaptor2[I1, I2, R](options, measurement, bySelector)

  override def apply(v1: I1, v2: I2): R = {
    val test = options.map(f => new ReferencedFunction[R]({ () => f.function(v1, v2) }, f.reference))
    val by = if (bySelector != null) bySelector(v1, v2) else 0
    Adaptive.tracker.runOption(test, by)
  }
}