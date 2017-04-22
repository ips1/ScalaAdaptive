package configuration

/**
  * Created by pk250187 on 4/22/17.
  */
trait BaseLongConfiguration extends BaseConfiguration {
  type MeasurementType = Long
  override val num = Numeric.LongIsIntegral
}
