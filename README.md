# Play-framework 1.2.x series support for HTTP GZIP compression

Author: [Jay Taylor][jaytaylor]
Date of first release: April 10, 2012

This module is hosted [on github](https://github.com/jaytaylor/play-gzip/ "Play-gzip on github")

## Description

This is a plugin to enable GZIP compression of HTTP responses for [Play-framework][]
applications of the 1.2.x variety.


## Requirements

A play-framework application running a [1.2.x series release][Play-framework-releases],
e.g. [play-1.2.4][].


## Adding the plugin to your project

First, add the gzip dependency and repository to `conf/dependencies.yml`:

    require:
        - play
        - play -> gzip 0.1

    repositories:
        - play-gzip:
            type: http
            artifact: "https://github.com/jaytaylor/jaytaylor-mvn-repo/raw/master/releases/play/[module]/[revision]/[module]-[revision].[ext]"
            contains:
                - play -> *

and run `play deps --sync --verbose` to retrieve the module.

Then you're ready to integrate GZIP support into your application!  This is as
simple as adding an import statement and extending the controller class with the
`Compress` trait.  For DRY purposes, this should usually be a base controller
trait which gets inherited by all other controllers.

    import play.modules.gzip.Compress

    class MyBaseControllerTrait extends Compress { ... }


## Configuration

Directives available for `conf/application.conf`:

    # Set to true to disable gzip compression (defaults to false)
    gzip.disabled=false

    # Set to true to disable gzip module logging (defaults to false)
    gzip.logging.disabled=false

If these directives are not present in your configuration, they will both
default to "false" (meaning that these features will be enabled.)


## Future iterations

    - Automatically detect the average compression ratio and optimize
      accordingly
    - Automatically detect the average response size in real-time as
      we go and then automatically optimize the buffer allocation
      sizes


## References

The development of this Play module has been fun, and there were a few documents
which helped greatly:

    - [engintekin's compression gist][engintekin]
    - [lights51's compression optimization commit][lights51]

[jaytaylor]: http://jaytaylor.com/ "Jay Taylor's website"
[Play-framework]: http://playframework.org/ "Play-framework"
[Play-framework-releases]: http://download.playframework.org/releases/ "Play-framework releases"
[play-1.2.4]: http://download.playframework.org/releases/play-1.2.4.zip "Play v1.2.4"
[engintekin]: https://gist.github.com/1317626 "engintekin's compression gist"
[lights51]: https://github.com/lights51/play/commit/a029b74a143464a1dec008b0de6d7ba7a75b8f20 "lights51's compression optimization commit"

