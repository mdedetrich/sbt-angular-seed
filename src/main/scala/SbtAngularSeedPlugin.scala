import sbt._
import Keys._

import org.json4s._
import org.json4s.jackson.JsonMethods._

object SbtAngularSeedKeys {
  val AngularSeed = config("AngularSeed") extend Compile
  val targetFile = SettingKey[File]("target-file","where the Angular JS seed is compiled to, please make sure extension is a js file")
  val jsonExpression = SettingKey[JValue]("json-expression","An expression that resolves to a JValue, who's result gets placed in the seed")
  val angularModuleName = SettingKey[String]("angular-module-name", "The name of the angular js module")
  val angularValueName = SettingKey[String]("angular-value-name","The name of the angular js value")
}

object SbtAngularSeedPlugin extends Plugin {
  import SbtAngularSeedKeys._

  val compileAngularSeed = TaskKey[Unit]("angularSeed","Compiles the AngularJS seed file")

  lazy val compileSbtAngularSeedTask = Def.task {
    val json = pretty(render((jsonExpression in AngularSeed).value))
    val moduleName = (angularModuleName in AngularSeed).value
    val angularName = (angularValueName in AngularSeed).value


    val firstString = """angular.module('""" + moduleName + """',[])""" + "\n"
    val secondString = """.value('""" + angularName + """',""" + "\n"
    val main = json + "\n"
    val end = """);"""

    val finalString =
      firstString + secondString + main + end

    IO.write((targetFile in AngularSeed).value,finalString)
  }

  val cleanAngularSeed = TaskKey[Unit]("clean","Deletes the AngularJS seed file")

  lazy val cleanAngularSeedTask = Def.task {
    IO.delete((targetFile in AngularSeed).value)
  }
  lazy val defaultSettings: Seq[Setting[_]] = Seq(
    angularModuleName in AngularSeed := "angularSeed",
    angularValueName in AngularSeed := (angularModuleName in AngularSeed).value.capitalize,
    targetFile in AngularSeed <<= Def.settingDyn{
      val fileName = (angularModuleName in AngularSeed).value
      resourceManaged (_ / "main" / "js" / (fileName + ".js"))
    },
    jsonExpression in AngularSeed := {
      JObject(List.empty)
    },
    compileAngularSeed in AngularSeed := compileSbtAngularSeedTask.value,
    cleanAngularSeed in AngularSeed := cleanAngularSeedTask.value
  )

}
