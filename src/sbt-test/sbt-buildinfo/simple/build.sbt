lazy val check = taskKey[Unit]("checks this plugin")

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    name := "helloworld",
    version := "0.1",
    scalaVersion := "2.10.2",
    buildInfoKeys := Seq(
      name,
      BuildInfoKey.map(version) { case (n, v) => "projectVersion" -> v.toDouble },
      scalaVersion,
      ivyXML,
      homepage,
      licenses,
      apiMappings,
      isSnapshot,
      "year" -> 2012,
      "sym" -> 'Foo,
      BuildInfoKey.action("buildTime") { 1234L },
      target
    ),
    buildInfoPackage := "hello",
    homepage := Some(url("http://example.com")),
    licenses := Seq("MIT License" -> url("https://github.com/sbt/sbt-buildinfo/blob/master/LICENSE")),
    check := {
      val f = (sourceManaged in Compile).value / "sbt-buildinfo" / ("%s.scala" format "BuildInfo")
      val lines = scala.io.Source.fromFile(f).getLines.toList
      lines match {
        case """package hello""" ::
             """""" ::
             """import java.io.File""" ::
             """import java.net.URL""" ::
             """""" ::
             """/** This object was generated by sbt-buildinfo. */""" ::
             """case object BuildInfo {""" ::
             """  /** The value is "helloworld". */"""::
             """  val name: String = "helloworld"""" ::
             """  /** The value is 0.1. */"""::
             """  val projectVersion = 0.1""" ::
             """  /** The value is "2.10.2". */""" ::
             """  val scalaVersion: String = "2.10.2"""" ::
             """  /** The value is Seq(). */""" ::
             """  val ivyXml: scala.xml.NodeSeq = Seq()""" ::
             """  /** The value is Some(new URL("http://example.com")). */""" ::
             """  val homepage: Option[URL] = Some(new URL("http://example.com"))""" ::
             """  /** The value is Seq(("MIT License" -> new URL("https://github.com/sbt/sbt-buildinfo/blob/master/LICENSE"))). */""" ::
             """  val licenses: Seq[(String, URL)] = Seq(("MIT License" -> new URL("https://github.com/sbt/sbt-buildinfo/blob/master/LICENSE")))""" ::
             """  /** The value is Map(). */""" ::
             """  val apiMappings: Map[File, URL] = Map()""" ::
             """  /** The value is false. */""" ::
             """  val isSnapshot: Boolean = false""" ::
             """  /** The value is 2012. */""" ::
             """  val year: Int = 2012""" ::
             """  /** The value is 'Foo. */""" ::
             """  val sym: scala.Symbol = 'Foo""" ::
             """  /** The value is 1234L. */""" ::
             """  val buildTime: Long = 1234L""" ::
             targetInfoComment ::
             targetInfo :: // """
             """  override val toString: String = "name: %s, projectVersion: %s, scalaVersion: %s, ivyXml: %s, homepage: %s, licenses: %s, apiMappings: %s, isSnapshot: %s, year: %s, sym: %s, buildTime: %s, target: %s" format (name, projectVersion, scalaVersion, ivyXml, homepage, licenses, apiMappings, isSnapshot, year, sym, buildTime, target)""" ::
             "" ::
             """  val toMap: Map[String, Any] = Map[String, Any](""" ::
             """    "name" -> name,""" ::
             """    "projectVersion" -> projectVersion,""" ::
             """    "scalaVersion" -> scalaVersion,""" ::
             """    "ivyXml" -> ivyXml,""" ::
             """    "homepage" -> homepage,""" ::
             """    "licenses" -> licenses,""" ::
             """    "apiMappings" -> apiMappings,""" ::
             """    "isSnapshot" -> isSnapshot,""" ::
             """    "year" -> year,""" ::
             """    "sym" -> sym,""" ::
             """    "buildTime" -> buildTime,""" ::
             """    "target" -> target)""" ::
             "" ::
             """  val toJson: String = toMap.map(i => "\"" + i._1 + "\":\"" + i._2 + "\"").mkString("{", ", ", "}")""" ::
             """}""" :: Nil if (targetInfo contains "val target: File = new File(") =>
        case _ => sys.error("unexpected output: \n" + lines.mkString("\n"))
      }
      ()
    }
  )
