package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.compat.java8.FunctionConverters._
import scala.concurrent.duration._
import java.util.function.Consumer
import scala.concurrent.Promise
import scala.util.Try

import org.graalvm.polyglot.Context
import org.graalvm.polyglot.{Source => GraalSource}
import org.graalvm.polyglot.HostAccess
import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def indexGraal() = Action.async { implicit request: Request[AnyContent] =>

    val polyfillContents = s"""
      var window = this;
      window.setTimeout = function (cb, time){ cb() };
      window.clearTimeout = function (){};
      """
    val context = Context
      .newBuilder("js")
      .allowAllAccess(true)
      .allowHostAccess(HostAccess.ALL)
      .allowIO(true)
      .build()
    context.eval("js", polyfillContents)

    val prom          = Promise[String]()
    val promiseJsCode = """
      |window.render = (input) => {
      |   return new Promise((resolve, reject) => {
      |     setTimeout(function () {
      |       resolve('Resolved from JS ' + input);
      |     }, 50);
      |   });
      | };
      |""".stripMargin

    context.eval(GraalSource.newBuilder("js", promiseJsCode, "").build())
      context
        .getBindings("js")
        .getMember("render")
        .execute("foo")
        .invokeMember("then", scalaCallback(prom))

    prom.future.map { content =>
      Ok(content)
    }
  }

  private def scalaCallback(promise: Promise[String]): Consumer[Object] =
    asJavaConsumer[Object](value => promise.complete(Try(value.toString())))
}
