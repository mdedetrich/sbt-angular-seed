# sbt-angular-seed: SBT Plugin for creating seeds for AngularJS

[sbt-angular-seed] is a SBT plugin designed to create seed data in the form of an [AngularJS] value contained inside a [AngularJS]
module. Note that this plugin has one job, create an [AngularJS] module (in the form of a file) out of an [SBT] task expression
that returns a [JSON4S] `JValue`, nothing more. It is up to the user to integrate this task as part of their build system

# Configuration

Add the following to your `project/build.sbt` file

First the repository

```scala
resolvers ++= Seq("mdedetrich-releases" at "http://artifactory.mdedetrich.com/plugins-release")
```

Then the plugin

```scala
addSbtPlugin("com.mdedetrich" % "sbt-angular-seed" % "1.0.0")
```

# Usage

There are 2 SBT keys which need to be changed for typical apps

* targetFile: This is the target destination for the compiled file. Must return a `File`
* jsonExpression: This is the task that generates the JSON which is placed in the seed file. Must return a `JValue`

There is also the task `angularSeed`, which creates the seed file

All of the tasks and keys are located in the `AngularSeed` config

As an example, this is what a trivial config may look like (`JsonDSL` is used to product the JSON, check [JSON4S] for more info)

```scala
val sbtAngularSeedSettings = SbtAngularSeedPlugin.defaultSettings ++ Seq(
    targetFile in AngularSeed <<= sourceDirectory (_ / "js" / "seed.js" ),
    jsonExpression in AngularSeed := {
        ("json" -> "is") ~ ("damn" -> "cool")
    }
)
```

Which would produce the following `seed.js` file

```javascript
angular.module('angularSeed',[])
.value('AngularSeed',
{
  "json" : "is",
  "damn" : "cool"
}
);
```

You would also probably want to make the `angularSeed` task to run when you do compile, which
can be done as follows

```scala
(compile in Compile) <<= (compile in Compile) dependsOn (compileSeed in AngularSeed)
```

Of course, this example is trivial. The main use of this plugin is to create client
side initial seed data, how the JValue from the `jsonExpression` task is created is up to the
user. You can do a database lookup to grab some seed data and output it as JSON, or you can
simply traverse static data structures in your code.

It is recommended to extend this [SBT] plugin with your own [SBT] plugin (which sets up how to
create the JSON seed), and then require that plugin in your actual project (mainly to deal
with possible dependency issues). Please consult the [SBT] docs on more info on how to
create plugins

# All Settings/Tasks/Configs

## Config

* AngularSeed: The config where all of the keys/settings are located

## Settings

* targetFile: Where the seed file gets created
* jsonExpression: The expression which produces the JSON containing the seed data
* angularModuleName: The name of the [AngularJS] module which gets created
* angularValueName: The name of the [AngularJS] value which gets created

## Tasks

* compileSeed: Compiles the [AngularJS] seed file
* clean: Deletes the [AngularJS] seed file


[sbt-angular-seed]:https://github.com/mdedetrich/sbt-angular-seed
[SBT]:http://www.scala-sbt.org/
[AngularJS]:http://angularjs.org/
[JSON4S]:https://github.com/json4s/json4s
