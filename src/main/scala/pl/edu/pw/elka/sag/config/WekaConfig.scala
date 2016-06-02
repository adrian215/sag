package pl.edu.pw.elka.sag.config

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConversions._

object Configuration {
  var configPath = "application.conf"
  //  val trainingFile = "D:\\projekty\\sag\\dane\\training.csv"
  //  val classificationFiles = Seq(
  //    ("Hilary", "D:\\training.csv"),
  //    ("Hilary", "D:\\training.csv"),
  //    ("Hilary", "D:\\training.csv")
  //    //    "D:\\projekty\\sag\\dane\\training.csv"
  //  )
  //  var delimiter = ","
  //  val svmModelFile = "D:\\projekty\\sag\\model\\svm_model.arff"
  //  val naiveBayesModelFile = "D:\\projekty\\sag\\model\\naiveBayes_model.arff"
  //  val svmFilterFile = "D:\\projekty\\sag\\model\\svm_filter.arff"
  //  val naiveBayesFilterFile = "D:\\projekty\\sag\\model\\naiveBayes_filter.arff"
  //
  //  //filter
  //  val wordsToKeep = 20000
  //
  //  //model
  //  val trainingSetSize: Float = 0.9f

  def setConfigPath(path: String): Unit = {
    configPath = path
  }

  def getConfig(): WekaConfig = {
    val file: Config = ConfigFactory.parseFile(new File(configPath))
    val classificationFiles = readClassificationFiles(file)

    WekaConfig(
      trainingFile = file.getString("trainingFile"),
      classificationFiles = classificationFiles,
      delimiter = file.getString("delimiter"),
      svmModelFile = file.getString("svmModelFile"),
      svmnlModelFile = file.getString("svmnlModelFile"),
      naiveBayesModelFile = file.getString("naiveBayesModelFile"),
      svmFilterFile = file.getString("svmFilterFile"),
      svmnlFilterFile = file.getString("svmFilterFile"),
      naiveBayesFilterFile = file.getString("naiveBayesFilterFile"),
      wordsToKeep = file.getInt("wordsToKeep"),
      trainingSetSize = file.getDouble("trainingSetSize").toFloat,
      costSVM = file.getDouble("costSVM"),
      start = file.getInt("start"),
      end = file.getInt("end"),
      step = file.getInt("step"),
      base = file.getInt("base"),
      startNl = file.getInt("startNl"),
      endNl = file.getInt("endNl"),
      stepNl = file.getInt("stepNl"),
      baseNl = file.getInt("baseNl")
    )
  }

  def readClassificationFiles(file: Config): Seq[(String, String)] = {
    val KEY: String = "classificationFiles"
    val NAME: String = "name"
    val FILE: String = "file"

    file.getConfigList(KEY)
      .map(record =>
        (record.getString(NAME), record.getString(FILE))
      )
      .toSeq
  }
}

case class WekaConfig(
                       val trainingFile: String,
                       val classificationFiles: Seq[(String, String)],
                       val delimiter: String,
                       val svmModelFile: String,
                       val svmnlModelFile: String,
                       val naiveBayesModelFile: String,
                       val svmFilterFile: String,
                       val svmnlFilterFile: String,
                       val naiveBayesFilterFile: String,
                       val wordsToKeep: Int,
                       val trainingSetSize: Float,
                       val costSVM: Double,

                       // optimize parameter
                       val start: Int,
                       val end: Int,
                       val step: Int,
                       val base: Int,
                       val startNl: Int,
                       val endNl: Int,
                       val stepNl: Int,
                       val baseNl: Int
                     )
