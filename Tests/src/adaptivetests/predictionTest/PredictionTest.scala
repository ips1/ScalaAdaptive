package adaptivetests.predictionTest

import adaptivetests.testmethods.TestMethods

import scala.util.Random
import scalaadaptive.api.Adaptive
import scalaadaptive.api.options.Storage
import scalaadaptive.core.configuration.Configuration
import scalaadaptive.core.configuration.blocks.{LimitedRegressionSelection, NoGrouping}
import scalaadaptive.core.configuration.defaults.FullHistoryTTestConfiguration

/**
  * Created by pk250187 on 6/7/17.
  */
object PredictionTest {
  def generateRandomDataOfRandomSize(maxSize: Int): List[Int] =
    generateRandomData(Random.nextInt(maxSize))

  def generateRandomData(size: Int): List[Int] =
    Seq.fill(size)(Random.nextInt).toList

  def runTestWithConfig(config: Configuration): Unit = {
    Adaptive.initialize(config)
    val testMethods = new TestMethods()
    //val runner = new TestRunner()
    //runner.runIncrementalTest(l => testMethods.functionContinuous(l))

    val testCount = 400
    val maxSize = 400
    val random = new Random(System.nanoTime)
    val trainData = Seq.fill(testCount)(generateRandomDataOfRandomSize(maxSize)).toList

    val function = testMethods.linearFunctionsContinuous storeUsing Storage.Global

    function.train(trainData)

    val verificationData = Seq.range(0, 400, 25).map(generateRandomData)

    verificationData.foreach(d => {
      function(d)
    })
  }

  def main(args: Array[String]): Unit = {
    val configurations = List(
      new FullHistoryTTestConfiguration with LimitedRegressionSelection with NoGrouping
      //new FullHistoryTTestConfiguration with NoGrouping
    )

    configurations.foreach(runTestWithConfig)
  }
}
