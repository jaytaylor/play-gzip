package play.modules.gzip

import GzipCompressionPlugin.{gzipDisabled, loggingDisabled}
import play.Logger
import play.mvc.{Controller, Finally}
import scala.collection.JavaConverters._
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.zip.GZIPOutputStream

/**
 * @author Jay Taylor [@jtaylor]
 *
 * @date 2012-04-02
 */
trait Compress {


    self: Controller =>

    /**
     * Apply GZIP compression to the response output.
     */
    @Finally
    def compress() {
        // Check whether or not module is enabled.
        gzipDisabled match {
            case true => Logger info "GZIP compression module disabled by configuration"

            case false =>
                // Look for the "Accept-Encoding" header.
                request.headers.asScala find { _._1.equalsIgnoreCase("accept-encoding") } match {
                    case Some((_, header)) =>
                        // Look for "gzip" as a supplied accepted encoding.
                        header.values.asScala mkString "," split ',' find { _.trim.equalsIgnoreCase("gzip") } match {
                            case Some(_) =>
                                val text = response.out.toString
                                val gzipped = gzip(text)
                                val bytesOut = gzipped.size

                                if (!loggingDisabled) {
                                    val bytesIn = (text getBytes "UTF-8").length

                                    // Print some stats to the log.
                                    val percentage = if (text.length == 0) {
                                        0
                                    } else {
                                        ((1 - (1.0 * gzipped.size / text.length)) * 100).round
                                    }
                                    Logger info "GZIP compression deflated " + bytesOut + "/" + bytesIn + " (" + percentage + "%)"
                                }

                                response.setHeader("Content-Encoding", "gzip")
                                response.setHeader("Content-Length", bytesOut.toString)
                                response.out = gzipped

                            case None => // Client did not support gzip responses.
                                Logger info "GZIP compression not possible - not supported by client ('gzip' not found in 'accept-encoding' header [value=" + header.toString + "])"
                        }

                    case None => // Client did not support gzip responses.
                        Logger info "GZIP compression not possible - not supported by client (\"accept-encoding\" was not specified)"
                }
        }
    }

    /**
     * Compress the input string using the GZIP algorithm.
     */
    def gzip(input: String): ByteArrayOutputStream = {
        val inputStream = new ByteArrayInputStream(input.getBytes)

        // Pre-allocate some bytes based on the optimistic assumption that
        // some amount of compression will occur 
        val hopingForCompressibility = (input.length * 0.50).toInt

        val byteArrayOut = new ByteArrayOutputStream(hopingForCompressibility)

        val gzipper = new GZIPOutputStream(byteArrayOut)

        val buf = new Array[Byte](4096)

        var numBytesRead = inputStream read buf
        while (numBytesRead > 0) {
            gzipper.write(buf, 0, numBytesRead)
            numBytesRead = inputStream read buf
        }

        // Clean up.
        inputStream.close
        gzipper.close

        byteArrayOut
    }
}

