package com.flecheck.hanabi.bga

object Utils {

  implicit class ExtendedInt (val i: Int) extends AnyVal {
    def +% (m: Int): Int = {val x = i % m; if (x < 0) x + m else x}
  }

  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
  def getUrl(url: String,
            connectTimeout: Int = 5000,
            readTimeout: Int = 5000,
            requestMethod: String = "GET",
            cookies: Map[String,String] = Map() ): String = {
    import java.net.{URL, HttpURLConnection}

    def cookiesStr: String = cookies.map { case (key, value) => key + "=" + value }.mkString("; ")

    val headers : Map[String,String] = Map("Host" -> "fr.boardgamearena.com",
      "User-Agent" -> "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:51.0) Gecko/20100101 Firefox/51.0",
      "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
      "Accept-Language" -> "en-US,en;q=0.5")

    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(connectTimeout)
    connection.setReadTimeout(readTimeout)
    connection.setRequestMethod(requestMethod)
    connection.setRequestProperty("Cookie",cookiesStr)
    //headers.foreach { case (key,data) => connection.setRequestProperty(key,data)}
    val inputStream = connection.getInputStream

    // Invalid UTF-8 encoding fix
    import java.nio.charset.CodingErrorAction
    import scala.io.Codec

    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    //

    val content = io.Source.fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close
    content
  }
}
