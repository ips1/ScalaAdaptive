package adaptivetests.jsonparsing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.google.gson.GsonBuilder
import flexjson.JSONDeserializer

import scalaadaptive.core.options.Selection

/**
  * Created by pk250187 on 6/1/17.
  */
class JsonParser[TData] {
  def parseWithFlexjson(json: String, targetClass: Class[TData]): TData = {
    new JSONDeserializer[TData]().deserialize(json, targetClass)
  }

  def parseWithGson(json: String, targetClass: Class[TData]): TData = {
    val builder = new GsonBuilder()
    val gson = builder.create()
    gson.fromJson(json, targetClass)
  }

  def parseWithJackson(json: String, targetClass: Class[TData]): TData = {
    val objectMapper = new ObjectMapper()
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper.readValue(json, targetClass)
  }

  import scalaadaptive.api.Implicits._

  val parse = (
    parseWithGson _ or parseWithJackson
    by ((json, c) => json.size)
    selectUsing Selection.Discrete
  )
}
