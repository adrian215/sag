package pl.edu.pw.elka.sag.config

object WekaConfig {
  val trainingFile = "D:\\projekty\\sag\\dane\\training.csv"
  val classificationFiles = Seq(
    "D:\\projekty\\sag\\dane\\HillaryClinton_2016-05-26.csv",
    "D:\\projekty\\sag\\dane\\training.csv"
  )
  var delimiter = ","
  val svmModelFile = "D:\\svm_model.arff"
  val naiveBayesModelFile = "D:\\naiveBayes_model.arff"
  val filterFile = "filter.arff"
}
