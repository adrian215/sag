package pl.edu.pw.elka.sag.config

object WekaConfig {
  val trainingFile = "D:\\training.csv"
  val classificationFiles = Seq(
    "D:\\training.csv",
    "D:\\training.csv"
  )
  var delimiter = ","
  val svmModelFile = "D:\\svm_model.arff"
  val naiveBayesModelFile = "D:\\naiveBayes_model.arff"
  val filterFile = "filter.arff"
}
