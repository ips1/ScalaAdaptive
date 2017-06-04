package adaptivetests.loadbalance

import java.time.Duration

import scalaadaptive.api.options.Selection
import scalaj.http.{Http, HttpOptions, HttpResponse}

/**
  * Created by pk250187 on 5/14/17.
  */
class WebApi(val baseUrl: String, val ports: Seq[Int]) {
  val route = "request"

  def buildUrl(port: Int) = s"http://$baseUrl:$port/$route"

  def performRequest(url: String)(query: String): Option[String] = {
    val response = Http(url)
      .param("data", query)
      .option(HttpOptions.readTimeout(20000))
      .asString
    if (response.code != 200) {
      None
    }
    else {
      Some(response.body)
    }
  }

  import scalaadaptive.api.Implicits._

  val performBalancedRequest = ports.tail.foldLeft(performRequest(buildUrl(ports.head)) _) { (f, port) =>
    f.or(performRequest(buildUrl(port)))
  } selectUsing Selection.Discrete limitedTo Duration.ofSeconds(20)
}
