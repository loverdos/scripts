import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import java.util.Date

import scala.annotation.tailrec
import com.sun.net.httpserver._

val port = args(0).toInt
val server = HttpServer.create(new InetSocketAddress(port), 1)

def log(x: HttpExchange): Unit = {
  val now = new Date()
  val remoteAddr = x.getRemoteAddress
  val method = x.getRequestMethod
  val uri = x.getRequestURI
  System.err.println(s"< [$now] $remoteAddr $method $uri")
}

object All200s extends HttpHandler {
  def handle(x: HttpExchange): Unit = {
    log(x)
    val in = x.getRequestBody
    val br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))

    @tailrec def read(): Unit =
      br.readLine() match {
        case null ⇒
          br.close()
        case line ⇒
          System.err.println(line)
          read()
      }

    read()
    x.sendResponseHeaders(200, 0)
    x.getResponseBody.close()
  }
}
object Kill extends HttpHandler {
  def handle(x: HttpExchange): Unit = {
    log(x)
    server.stop(0)
  }
}

server.createContext("/", All200s)
server.createContext("/kill", Kill)
server.setExecutor(null)
System.err.println(s"Serving / on port $port. Will always return code 200. Send /kill to shut me down.")
server.start()
