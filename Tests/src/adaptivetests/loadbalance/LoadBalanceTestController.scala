package adaptivetests.loadbalance

import scalaj.http.{Http, HttpOptions, HttpResponse}

/**
  * Created by pk250187 on 5/14/17.
  */
class LoadBalanceTestController {
  val ports = List(3123, 3124)
  val baseUrl = "localhost"

  val testData = "TEST"

  val api = new WebApi(baseUrl, ports)

  def buildUrl(port: Int, command: String) = s"http://$baseUrl:$port/$command"

  private def executeCommand(port: Int, command: String) = Http(buildUrl(port, command))
    .postData("")
    .option(HttpOptions.readTimeout(20000))
    .asString

  def increaseLoad(port: Int): Unit = executeCommand(port, "increaseLoad")
  def decreaseLoad(port: Int): Unit = executeCommand(port, "decreaseLoad")

  def sendRequest(): Unit = api.performBalancedRequest(testData)
}
