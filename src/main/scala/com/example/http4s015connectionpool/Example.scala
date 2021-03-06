package com.example.http4s015connectionpool

import org.http4s._
import org.http4s.client.Client
import org.http4s.dsl._
import scalaz.concurrent.Task

object Example {

  def service(client: Client) = HttpService {
      case GET -> Root / "client" / "run" / IntVar(n) =>
        val runRequests = Task.gatherUnordered((1 to n).toList.map( _ =>
          client.expect[String](Uri.unsafeFromString("https://example.com"))
        ))
        for {
          start <- Task(System.currentTimeMillis())
          _ <- runRequests
          end <- Task(System.currentTimeMillis())
          resp <- Ok(s"Took ${end - start} milliseconds to run $n requests")
        } yield resp
    }
}
