This is standart play framework application generated from play-scala-seed template.

To make custom jre for this project run
`sbt stage`
which generates artifacts in `/target/universal/stage`

```
stage % ls -la
total 8
drwxr-xr-x   9 dmitriy  staff   288 Apr  9 18:12 .
drwxr-xr-x   4 dmitriy  staff   128 Apr  9 17:47 ..
-rw-r--r--   1 dmitriy  staff     5 Apr  9 18:12 RUNNING_PID
drwxr-xr-x   4 dmitriy  staff   128 Apr  9 17:47 bin //Application run script
drwxr-xr-x   6 dmitriy  staff   192 Apr  9 18:12 conf
drwxr-xr-x   7 dmitriy  staff   224 Apr  9 17:47 jre //This is your custom JRE
drwxr-xr-x  64 dmitriy  staff  2048 Apr  9 18:12 lib
drwxr-xr-x   3 dmitriy  staff    96 Apr  9 17:55 logs
drwxr-xr-x   3 dmitriy  staff    96 Apr  9 17:47 share
```
To simplify stage + run process you can run
`sbt runProd`.
then go to `http://localhost:9000/graal`

Stage result for different JVM:
- AdoptOpenJDK 11.0.9.hs: Build ok. Application is working properly.
- GraalVM 21.0.0.2.r11: Build error:
```
[info] Running: jlink --output /Users/dmitriy/projects/scala/play-scala-graalvm-jlink/target/jlink/output --add-modules java.logging,java.management,java.naming,java.compiler,jdk.jfr,java.base,java.desktop,java.transaction.xa,java.rmi,java.sql,java.datatransfer,java.xml,jdk.crypto.ec,jdk.unsupported,jdk.jdwp.agent --module-path /Users/dmitriy/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/com/thoughtworks/paranamer/paranamer/2.8/paranamer-2.8.jar --no-header-files --no-man-pages --compress=2
[info] Error: Hash of java.rmi (bb68a77816d9b03846e06607d7addb24e4ba4dc568898cd97cf21a6547ddfe38) differs to expected hash (31d47a2989873e5fef34bd4481f1bfd864ce36085263a738b2f958244ecccc47) recorded in java.base
[error] java.lang.RuntimeException: Nonzero exit value: 1
[error]         at scala.sys.package$.error(package.scala:30)
[error]         at com.typesafe.sbt.packager.archetypes.jlink.JlinkPlugin$.runForOutput(JlinkPlugin.scala:232)
[error]         at com.typesafe.sbt.packager.archetypes.jlink.JlinkPlugin$.$anonfun$runJavaTool$2(JlinkPlugin.scala:217)
[error]         at sbt.io.IO$.withTemporaryFile(IO.scala:533)
[error]         at sbt.io.IO$.withTemporaryFile(IO.scala:543)
[error]         at com.typesafe.sbt.packager.archetypes.jlink.JlinkPlugin$.runJavaTool(JlinkPlugin.scala:211)
[error]         at com.typesafe.sbt.packager.archetypes.jlink.JlinkPlugin$.$anonfun$projectSettings$28(JlinkPlugin.scala:164)
[error]         at com.typesafe.sbt.packager.archetypes.jlink.JlinkPlugin$.$anonfun$projectSettings$26(JlinkPlugin.scala:169)
[error]         at scala.Function1.$anonfun$compose$1(Function1.scala:49)
[error]         at sbt.internal.util.$tilde$greater.$anonfun$$u2219$1(TypeFunctions.scala:62)
[error]         at sbt.std.Transform$$anon$4.work(Transform.scala:67)
[error]         at sbt.Execute.$anonfun$submit$2(Execute.scala:281)
[error]         at sbt.internal.util.ErrorHandling$.wideConvert(ErrorHandling.scala:19)
[error]         at sbt.Execute.work(Execute.scala:290)
[error]         at sbt.Execute.$anonfun$submit$1(Execute.scala:281)
[error]         at sbt.ConcurrentRestrictions$$anon$4.$anonfun$submitValid$1(ConcurrentRestrictions.scala:178)
[error]         at sbt.CompletionService$$anon$2.call(CompletionService.scala:37)
[error]         at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
[error]         at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:515)
[error]         at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
[error]         at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
[error]         at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
[error]         at java.base/java.lang.Thread.run(Thread.java:834)
[error] (jlinkBuildImage) Nonzero exit value: 1
```
Looks similar to this bug: https://github.com/AdoptOpenJDK/openjdk-tests/issues/1293

- GraalVM 20.0.0.r11: Build ok. When you run app there is error:
```
  ! @7jbfo42on - Internal server error, for (GET) [/graal] ->
 
play.api.UnexpectedException: Unexpected exception[RuntimeException: java.lang.NoClassDefFoundError: org/graalvm/polyglot/Context]
        at play.api.http.HttpErrorHandlerExceptions$.throwableToUsefulException(HttpErrorHandler.scala:358)
        at play.api.http.DefaultHttpErrorHandler.onServerError(HttpErrorHandler.scala:264)
        at play.core.server.AkkaHttpServer$$anonfun$2.applyOrElse(AkkaHttpServer.scala:430)
        at play.core.server.AkkaHttpServer$$anonfun$2.applyOrElse(AkkaHttpServer.scala:422)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:454)
        at akka.dispatch.BatchingExecutor$AbstractBatch.processBatch(BatchingExecutor.scala:63)
        at akka.dispatch.BatchingExecutor$BlockableBatch.$anonfun$run$1(BatchingExecutor.scala:100)
        at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
        at scala.concurrent.BlockContext$.withBlockContext(BlockContext.scala:94)
        at akka.dispatch.BatchingExecutor$BlockableBatch.run(BatchingExecutor.scala:100)
        at akka.dispatch.TaskInvocation.run(AbstractDispatcher.scala:49)
        at akka.dispatch.ForkJoinExecutorConfigurator$AkkaForkJoinTask.exec(ForkJoinExecutorConfigurator.scala:48)
        at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1020)
        at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1656)
        at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1594)
        at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
Caused by: java.lang.RuntimeException: java.lang.NoClassDefFoundError: org/graalvm/polyglot/Context
        at play.api.mvc.ActionBuilder$$anon$9.apply(Action.scala:384)
        at play.api.mvc.Action.$anonfun$apply$4(Action.scala:82)
        at play.api.libs.streams.StrictAccumulator.$anonfun$mapFuture$4(Accumulator.scala:168)
        at scala.util.Try$.apply(Try.scala:210)
        at play.api.libs.streams.StrictAccumulator.$anonfun$mapFuture$3(Accumulator.scala:168)
        at scala.Function1.$anonfun$andThen$1(Function1.scala:85)
        at scala.Function1.$anonfun$andThen$1(Function1.scala:85)
        at play.api.libs.streams.StrictAccumulator.run(Accumulator.scala:199)
        at play.core.server.AkkaHttpServer.$anonfun$runAction$4(AkkaHttpServer.scala:417)
        at akka.http.scaladsl.util.FastFuture$.strictTransform$1(FastFuture.scala:41)
        at akka.http.scaladsl.util.FastFuture$.$anonfun$transformWith$3(FastFuture.scala:51)
        at scala.concurrent.impl.Promise$Transformation.run(Promise.scala:448)
        ... 12 common frames omitted
Caused by: java.lang.NoClassDefFoundError: org/graalvm/polyglot/Context
        at controllers.HomeController.$anonfun$indexGraal$1(HomeController.scala:36)
        at play.api.mvc.ActionBuilderImpl.invokeBlock(Action.scala:441)
        at play.api.mvc.ActionBuilderImpl.invokeBlock(Action.scala:439)
        at play.api.mvc.ActionBuilder$$anon$9.apply(Action.scala:379)
        ... 23 common frames omitted
Caused by: java.lang.ClassNotFoundException: org.graalvm.polyglot.Context
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)
        ... 27 common frames omitted
```