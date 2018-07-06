package io.github.spf3000.hutsapi

import cats.effect.IO
import cats.Monad
import cats.FlatMap
import cats.implicits._
import cats.effect._
import fs2.StreamApp
import fs2.Stream
import io.circe.generic.auto._
import io.circe.syntax._

import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.dsl.io._

import scala.concurrent.ExecutionContext.Implicits.global

import entities.Golden
import XParse._

object XmlServer extends StreamApp[IO] with Http4sDsl[IO] {

  val service = HttpService[IO] {

    case req @ POST -> Root / "transformer" =>
         req.decodeJson[Golden]
           .flatMap(gold => IO {fx(gold.goldenText)})
           .flatMap(annotations => Response(status = Status.Created).withBody(annotations.asJson))

  }

  def stream(args: List[String], requestShutdown: IO[Unit]) =
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .mountService(service, "/")
      .serve
    }
