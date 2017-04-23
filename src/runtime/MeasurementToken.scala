package runtime

/**
  * Created by pk250187 on 4/23/17.
  */
trait MeasurementToken {
  def runMeasuredFunction[TReturnValue](fun: () => TReturnValue): TReturnValue
}
