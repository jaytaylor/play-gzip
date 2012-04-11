package controllers

import play.Logger
import play.mvc.{Before, Controller, Finally}
import play.modules.gzip.Compress
import org.joda.time.DateTime

/**
 * @author Jay Taylor [@jtaylor]
 *
 * @date 2011-05-23
 */

trait DefaultController extends Compress {

    self: Controller =>

    @Before
    def setDefaults {
        Logger info "Incoming request :: Action=" + request.action + ", params=" + request.params.allSimple
        renderArgs += "started" -> new DateTime
    }

    @Finally
    def finish {
        val started = renderArgs.get[DateTime]("started", classOf[DateTime])
        val ended = new DateTime

        Option(started) match {
            case Some(started: DateTime) =>
                val duration = ended.getMillis - started.getMillis
                Logger info "Request took " + duration + "ms) :: Action=" + request.action + ", params=" + request.params.allSimple

            case _ =>
                Logger warn "[WEIRD] For some reason renderArgs.get(\"started\") was null! :: Action=" + request.action + ", params=" + request.params.allSimple
        }
    }
}

