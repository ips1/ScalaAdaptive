package adaptivetests.training

import adaptivetests.TestRunner
import tools.methods.TestMethods

import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.blocks.selection.LoessInterpolationInputBasedStrategy
import scalaadaptive.core.configuration.blocks.storage.CachedGroupStorage
import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by Petr Kubat on 5/7/17.
  */
object TrainingTest {
  def main(args: Array[String]): Unit = {

    val configurations = List(new DefaultConfiguration with LoessInterpolationInputBasedStrategy with CachedGroupStorage)
    val runner = new TestRunner()
    val testMethods = new TestMethods()

    configurations.foreach(cfg => {
      Adaptive.initialize(cfg)

      val data = Seq.range(0, 40).map(i => runner.customData(i * 8))

      testMethods.function.train(data)

      println()
      println("==================")
      println("Training complete!")
      println("==================")
      println()

      runner.runIncrementalTest(l => testMethods.function(l))
    })
  }

}
