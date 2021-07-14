This is a play framework application with graalvm server-side javascript and custom JRE build using Jlink.

Make sure you use GraalVM to run sbt. If you use `sdkman` you can switch yourt java version with command like this:
`sdk use java 21.2.r11-grl`

```
% java -version 
openjdk version "11.0.11" 2021-04-20
OpenJDK Runtime Environment GraalVM CE 21.2.0-dev (build 11.0.11+8-jvmci-21.1-b05)
OpenJDK 64-Bit Server VM GraalVM CE 21.2.0-dev (build 11.0.11+8-jvmci-21.1-b05, mixed mode, sharing)
```

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

Check that generated JRE is the same as host JRE:
```
stage % ./jre/bin/java -version
openjdk version "11.0.11" 2021-04-20
OpenJDK Runtime Environment (build 11.0.11+8-jvmci-21.1-b05)
OpenJDK 64-Bit Server VM (build 11.0.11+8-jvmci-21.1-b05, mixed mode)
```
Ok, we have JRE with GraalVMs jvmci-21.1-b05.

Run it:
```
stage % ./bin/play-scala-graalvm-jlink -XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler
```
And go to `http://localhost:9000/graal`
If you see `Resolved from JS foo` congrats! All worked as planed and you have Server-side Javascript running 
with GraalVMs JS Engine.