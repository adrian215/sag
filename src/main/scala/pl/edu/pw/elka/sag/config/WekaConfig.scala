package pl.edu.pw.elka.sag.config

object WekaConfig {
  val trainingFile = "D:\\training.csv"
  val classificationFiles = Seq(
    ("Hilary", "D:\\training.csv"),
    ("Hilary", "D:\\training.csv"),
    ("Hilary", "D:\\training.csv")
//    "D:\\projekty\\sag\\dane\\training.csv"
  )
  var delimiter = ","
  val svmModelFile = "D:\\svm_model.arff"
  val naiveBayesModelFile = "D:\\naiveBayes_model.arff"
  val filterFile = "filter.arff"
}
