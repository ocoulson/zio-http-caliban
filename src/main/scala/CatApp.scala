import zio._
import zhttp.http._
import zhttp.service._
import caliban.GraphQL.graphQL
import caliban.RootResolver
import caliban.schema.{ ArgBuilder, Schema }
import caliban.CalibanError.ExecutionError
import caliban.GraphQLRequest
import io.circe.Json
import io.circe.syntax._

import services.{CatService, CatStore}
import models.{Queries, Mutations, Cat}


import scala.util.Try
import java.net.URL
import Extensions._

object CatApp extends zio.App {

  implicit val urlSchema: Schema[Any, URL] = Schema.stringSchema.contramap(_.toString)
  implicit val urlArgBuilder: ArgBuilder[URL] = ArgBuilder.string.flatMap(
    url => Try(new URL(url)).fold(_ => Left(ExecutionError(s"Invalid URL $url")), Right(_))
  )

  val catService: CatService = new CatStore

  val queries = Queries(
    catService.listCats,
    args => catService.findCat(args.name),
    catService.randomCatPicture
  )

  val mutations = Mutations(
    args => catService.addCat(args.cat),
    args => catService.editCatPicture(args.name, args.picUrl)
  )
  
  val api = graphQL(RootResolver(queries, mutations))

  def executeRequest(request: GraphQLRequest) = for {
    interpreter <- api.interpreter
    res <- interpreter.executeRequest(request)
  } yield Response.jsonString(res.data.toString)

  val app: HttpApp[Any, Nothing] = Http.collectM[Request] {
    case Method.GET -> Root / "schema" => UIO(Response.text(api.render))
    case Request(endpoint,data) if endpoint._1 == Method.POST && endpoint._2._1 == Root / "graphql" => 
      data.getGraphQlBody
        .flatMap(req => executeRequest(req))
        .catchAll(err => UIO(Response.fromHttpError(HttpError.BadRequest(err.getMessage))))
  }

  private val PORT = 8090

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    Server.start(PORT, app).exitCode
  }
}

object Extensions {
  
  extension (d: Request.Data)
    def getGraphQlBody: Task[GraphQLRequest] = d.content match {
      case HttpData.CompleteData(data) => 
        ZIO
          .fromEither(Option(data.map(_.toChar).mkString).toRight(new Exception("Error Getting data")))
          .flatMap(jsonString => ZIO.fromEither(io.circe.parser.decode[GraphQLRequest](jsonString)))
        
      case _                           => ZIO.fail(new Exception("Incomplete data"))
    }

}
