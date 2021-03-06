package adaptivetests.jsonparsing

import java.io.PrintWriter

import scala.io.Source
import data.PersonList

/**
  * Created by Petr Kubat on 6/1/17.
  */
object Test {
  def main(args: Array[String]): Unit = {
    val parser = new JsonParser[PersonList]()
    val bigJsonString = Source.fromFile("Tests/src/adaptivetests/jsonparsing/BigJsonData.json").mkString
    val smallJsonString = Source.fromFile("Tests/src/adaptivetests/jsonparsing/SmallJsonData.json").mkString
    val superBigJsonString = Source.fromFile("Tests/src/adaptivetests/jsonparsing/SuperBigJsonData.json").mkString

    Seq.range(0, 60).foreach(i => parser.parse(bigJsonString, classOf[PersonList]))
    Seq.range(0, 60).foreach(i => parser.parse(smallJsonString, classOf[PersonList]))
    Seq.range(0, 60).foreach(i => parser.parse(superBigJsonString, classOf[PersonList]))

    parser.parse.getAnalyticsData.foreach(_.saveData(new PrintWriter(System.out)))

    println("DONE!")
  }
}
