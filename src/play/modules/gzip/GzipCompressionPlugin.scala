package play.modules.gzip

import play.{configuration, Logger, PlayPlugin}

/**
 * GZIP Compression Plugin
 *
 * @author Jay Taylor [@jtaylor]
 * @date 2012-04-06
 */

object GzipCompressionPlugin {
    val gzipDisabled = configuration("gzip.disabled", "false").toBoolean
    val loggingDisabled = configuration("gzip.logging.disabled", "false").toBoolean
}

class GzipCompressionPlugin extends PlayPlugin {

    import GzipCompressionPlugin._

    /**
     * Play Framework Hook: onApplicationStart
     */
    override def onApplicationStart: Unit =
        Logger info "GZIP deflate module started (gzipDisabled=" + gzipDisabled + ", loggingDisabled=" + loggingDisabled + ")"
}

