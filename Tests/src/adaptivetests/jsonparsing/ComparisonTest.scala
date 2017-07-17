package adaptivetests.jsonparsing

import java.io.PrintWriter

import adaptivetests.jsonparsing.data.PersonList

import scala.io.Source
import scalaadaptive.api.Adaptive
import scalaadaptive.core.configuration.blocks.{CachedStatisticsStorage, NoLogging}
import scalaadaptive.core.configuration.defaults.DefaultConfiguration

/**
  * Created by Petr Kubat on 6/19/17.
  */
object ComparisonTest {
  private def measureParserRun(parser: (String, Class[PersonList]) => PersonList,
                                       data: String): Long = {
    val startTime = System.nanoTime()

    val result = parser(data, classOf[PersonList])

    System.nanoTime() - startTime
  }

  private def runTestWithData(name: String,
                              data: String,
                              repeatCount: Int) = {
    var totalGson: Long = 0
    var totalJackson: Long = 0
    var totalCombined: Long = 0
    val parser = new JsonParser[PersonList]()

    // Initial runs to avoid structure intialization etc.
    Seq.range(0, 20).foreach(i => {
      parser.parseWithGson(data, classOf[PersonList])
      parser.parseWithJackson(data, classOf[PersonList])
    })

    Seq.range(0, repeatCount).foreach(i => {
      totalGson += measureParserRun(parser.parseWithGson, data)
      totalJackson += measureParserRun(parser.parseWithJackson, data)
      totalCombined += measureParserRun(parser.parse, data)
    })

    val gsonMilis = totalGson.toDouble / (1000 * 1000)
    val jacksonMilis = totalJackson.toDouble / (1000 * 1000)
    val combinedMilis = totalCombined.toDouble / (1000 * 1000)

    println(s"$name - GSON - total: $gsonMilis, average: ${gsonMilis / repeatCount}")
    println(s"$name - Jackson - total: $jacksonMilis, average: ${jacksonMilis / repeatCount}")
    println(s"$name - combined - total: $combinedMilis, average: ${combinedMilis / repeatCount}")
  }

  def main(args: Array[String]): Unit = {
    val bigJsonString = Source.fromFile("Tests/src/adaptivetests/jsonparsing/BigJsonData.json").mkString
    val smallJsonString = Source.fromFile("Tests/src/adaptivetests/jsonparsing/SmallJsonData.json").mkString
    val superBigJsonString = Source.fromFile("Tests/src/adaptivetests/jsonparsing/SuperBigJsonData.json").mkString

    Adaptive.initialize(new DefaultConfiguration with CachedStatisticsStorage)

    val smallRepeatCount = 10000
    val bigRepeatCount = 5000
    val superBigRepeatCount = 200

    // Small JSON
    runTestWithData("Small", smallJsonString, smallRepeatCount)

    // Big JSON
    runTestWithData("Big", bigJsonString, bigRepeatCount)

    // Super big JSON
    runTestWithData("Super big", superBigJsonString, superBigRepeatCount)

    println("DONE!")
  }
}
