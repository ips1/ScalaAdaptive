package functionadaptors

import options.Measurement.Measurement
import options.Storage.Storage
import options.{Measurement, RunOption, Storage}
import runtime.{Adaptive, ReferencedFunction}

/**
  * Created by pk250187 on 3/19/17.
  */
class FunctionAdaptor0[R](private val options: List[RunOption[() => R]],
                          private val using: Measurement = Measurement.RunTime,
                          private val bySelector: () => Int = null,
                          protected val storage: Storage = Storage.Global) extends (() => R) with CustomRunner {

  def or(fun: () => R): () => R = orAdaptor(Implicits.toAdaptor(fun))
  private def orAdaptor(fun: FunctionAdaptor0[R]): () => R = new FunctionAdaptor0[R](this.options ++ fun.options)
  def by(selector: () => Int): () => R = new FunctionAdaptor0[R](options, using, selector)
  def using(measurement: Measurement): () => R = new FunctionAdaptor0[R](options, measurement, bySelector)

  override def apply(): R = {
    val test = options.map(f => new ReferencedFunction[R]({ () => f.function() }, f.reference))
    val by = if (bySelector != null) bySelector() else 0
    runOption(test, by)
  }
}