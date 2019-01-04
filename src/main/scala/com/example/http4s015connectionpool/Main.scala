package com.example.http4s015connectionpool

import org.http4s.client.blaze.{BlazeClientConfig, PooledHttp1Client}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.{Server, ServerApp}
import scalaz.concurrent.Task

import scala.concurrent.duration._

object Main extends ServerApp {
  override def server(args: List[String]): Task[Server] = {

    val client = PooledHttp1Client(
      maxTotalConnections = 50,
      BlazeClientConfig.defaultConfig.copy(
        idleTimeout = 59.second,
        requestTimeout = 60.seconds
      )
    )

    BlazeBuilder
      .bindHttp(8080, "localhost")
      .mountService(Example.service(client), "/benchmark")
      .start
  }
}

