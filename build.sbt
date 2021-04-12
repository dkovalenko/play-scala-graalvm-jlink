name := """play-scala-graalvm-jlink"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, JlinkPlugin)

Global / onChangedBuildSource := ReloadOnSourceChanges

scalaVersion := "2.13.5"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

val isGraalVM = System.getProperty("java.vendor.version").contains("GraalVM")

val graalVersion = "21.0.0.2"
libraryDependencies ++= (
  if (isGraalVM) {
    Seq.empty
  } else
    Seq(
      "org.graalvm.js"      % "js",
      "org.graalvm.sdk"     % "graal-sdk",
      "org.graalvm.tools"   % "chromeinspector",
      "org.graalvm.truffle" % "truffle-api"
    ).map(_ % graalVersion)
)

jlinkOptions ++= Seq(
  "--no-header-files",
  "--no-man-pages",
  "--compress=2"
)

jlinkModules ++= Seq(
  "jdk.crypto.ec",
  "jdk.unsupported",
  "jdk.jdwp.agent"
)
fullClasspath in jlinkBuildImage := {
      (fullClasspath in Compile).value
        .filterNot { clazz =>
          clazz.data.toString().contains("log4j-api")
        }
    }

jlinkModulePath := {
  fullClasspath
    .in(jlinkBuildImage)
    .value
    .filter { item =>
      item.get(moduleID.key).exists { modId =>
        modId.name == "paranamer"
      } 
      //|| item.data.toString().contains("javax.activation")
    }
    .map(_.data)
}

jlinkIgnoreMissingDependency := JlinkIgnore.only(
  "ch.qos.logback.classic" -> "javax.servlet.http",
  "ch.qos.logback.classic.boolex" -> "groovy.lang",
  "ch.qos.logback.classic.boolex" -> "org.codehaus.groovy.control",
  "ch.qos.logback.classic.boolex" -> "org.codehaus.groovy.reflection",
  "ch.qos.logback.classic.boolex" -> "org.codehaus.groovy.runtime",
  "ch.qos.logback.classic.boolex" -> "org.codehaus.groovy.runtime.callsite",
  "ch.qos.logback.classic.boolex" -> "org.codehaus.groovy.runtime.typehandling",
  "ch.qos.logback.classic.gaffer" -> "groovy.lang",
  "ch.qos.logback.classic.gaffer" -> "org.codehaus.groovy.control",
  "ch.qos.logback.classic.gaffer" -> "org.codehaus.groovy.control.customizers",
  "ch.qos.logback.classic.gaffer" -> "org.codehaus.groovy.reflection",
  "ch.qos.logback.classic.gaffer" -> "org.codehaus.groovy.runtime",
  "ch.qos.logback.classic.gaffer" -> "org.codehaus.groovy.runtime.callsite",
  "ch.qos.logback.classic.gaffer" -> "org.codehaus.groovy.runtime.typehandling",
  "ch.qos.logback.classic.gaffer" -> "org.codehaus.groovy.runtime.wrappers",
  "ch.qos.logback.classic.gaffer" -> "org.codehaus.groovy.transform",
  "ch.qos.logback.classic.helpers" -> "javax.servlet",
  "ch.qos.logback.classic.helpers" -> "javax.servlet.http",
  "ch.qos.logback.classic.selector.servlet" -> "javax.servlet",
  "ch.qos.logback.classic.servlet" -> "javax.servlet",
  "ch.qos.logback.core.boolex" -> "org.codehaus.janino",
  "ch.qos.logback.core.joran.conditional" -> "org.codehaus.commons.compiler",
  "ch.qos.logback.core.joran.conditional" -> "org.codehaus.janino",
  "ch.qos.logback.core.net" -> "javax.mail",
  "ch.qos.logback.core.net" -> "javax.mail.internet",
  "ch.qos.logback.core.status" -> "javax.servlet",
  "ch.qos.logback.core.status" -> "javax.servlet.http",
  "io.jsonwebtoken.impl" -> "android.util",
  "io.jsonwebtoken.impl.crypto" -> "org.bouncycastle.jce",
  "io.jsonwebtoken.impl.crypto" -> "org.bouncycastle.jce.spec",
  "javax.transaction" -> "javax.enterprise.context",
  "javax.transaction" -> "javax.enterprise.util",
  "javax.transaction" -> "javax.interceptor",
  "org.joda.time" -> "org.joda.convert",
  "org.joda.time.base" -> "org.joda.convert",
)