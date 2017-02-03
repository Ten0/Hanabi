package com.flecheck.hanabi.bga

object Utils {
  @throws(classOf[java.io.IOException])
  @throws(classOf[java.net.SocketTimeoutException])
  def getUrl(url: String,
            connectTimeout: Int = 5000,
            readTimeout: Int = 5000,
            requestMethod: String = "GET",
            cookies: Map[String,String] = Map() ): String = {
    import java.net.{URL, HttpURLConnection}

    def cokkiesStr: String = cookies.map { case (key, value) => key + "=" + value }.mkString("; ")

    val connection = new URL(url).openConnection.asInstanceOf[HttpURLConnection]
    connection.setConnectTimeout(connectTimeout)
    connection.setReadTimeout(readTimeout)
    connection.setRequestMethod(requestMethod)
    connection.setRequestProperty("cokkie",cokkiesStr)
    val inputStream = connection.getInputStream
    val content = io.Source.fromInputStream(inputStream).mkString
    if (inputStream != null) inputStream.close
    content
  }
}
