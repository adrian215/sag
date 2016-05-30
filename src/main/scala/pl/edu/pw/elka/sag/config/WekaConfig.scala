package pl.edu.pw.elka.sag.config

object WekaConfig {
  val trainingFile = "D:\\projekty\\sag\\dane\\training.csv"
  val classificationFiles = Seq(
    ("Hilary", "D:\\training.csv"),
    ("Hilary", "D:\\training.csv"),
    ("Hilary", "D:\\training.csv")
//    "D:\\projekty\\sag\\dane\\training.csv"
  )
  var delimiter = ","
  val svmModelFile = "D:\\projekty\\sag\\model\\svm_model.arff"
  val naiveBayesModelFile = "D:\\projekty\\sag\\model\\naiveBayes_model.arff"
  val svmFilterFile = "D:\\projekty\\sag\\model\\svm_filter.arff"
  val naiveBayesFilterFile = "D:\\projekty\\sag\\model\\naiveBayes_filter.arff"

  //filter
  val wordsToKeep = 20000

  //model
  val trainingSetSize : Float =  0.9f
}
