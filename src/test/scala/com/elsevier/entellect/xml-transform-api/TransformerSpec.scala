package io.github.spf3000.hutsapi


import cats._
import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.Json
import io.github.spf3000.hutsapi.entities._
import org.http4s.circe._
import org.http4s._
import org.http4s.implicits._
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import scala.io.Source
import scala.collection.mutable.ListBuffer


class TransformerSpec extends Specification {


  "Post Xmls" >> {
    "return 200" >> {
      postXmlReturns201()
    }

    "return annotations" >>
      postXmlReturnsAnnotations
  }

  val gold = Golden("<week><day>Mon</day><day>Tues</day></week>")

  def testService(): HttpService[IO] = XmlServer.service

  private[this] val retPostXml: Response[IO] =  {
    val postLstngs = Request[IO](Method.POST, Uri.uri("/transformer")).withBody(gold.asJson).unsafeRunSync()
    testService().orNotFound(postLstngs).unsafeRunSync()
  }

  private[this] def postXmlReturns201(): MatchResult[Status] =
    retPostXml.status must beEqualTo(Status.Created)

  private[this] def postXmlReturnsAnnotations(): MatchResult[String] =
    retPostXml.as[String].unsafeRunSync must beEqualTo("""[{"name":"week","start":0,"end":7},{"name":"day","start":0,"end":3},{"name":"day","start":3,"end":7}]""")

}
