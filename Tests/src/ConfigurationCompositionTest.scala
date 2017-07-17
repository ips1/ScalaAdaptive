import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.blocks.storage.{CachedGroupStorage, CachedRegressionStorage, CachedStatisticsStorage}
import scalaadaptive.core.runtime.AdaptiveInternal

/**
  * Created by Petr Kubat on 7/17/17.
  */
object ConfigurationCompositionTest {
  def main(args: Array[String]): Unit = {
    import scalaadaptive.core.configuration.defaults.DefaultConfiguration
    val config = new DefaultConfiguration
      with CachedRegressionStorage
      with CachedGroupStorage
      with CachedStatisticsStorage

    Adaptive.initialize(config)

    val internal = AdaptiveInternal

    import scalaadaptive.api.Implicits._

    val fastHello = () => println("Hello World!")
    val slowHello = () => {
      Thread.sleep(10)
      println("Sloooooow Hello World!")
    }

    val hello = fastHello or slowHello

    hello()
    println("Test")
  }
}
