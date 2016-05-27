//package pl.edu.pw.elka.sag;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import weka.classifiers.functions.LibSVM;
//import weka.core.Attribute;
//import weka.core.DenseInstance;
//import weka.core.Instances;
//import weka.core.SelectedTag;
//import weka.core.converters.ArffLoader;
//import weka.core.converters.LibSVMLoader;
//import weka.core.converters.Loader;
//import weka.core.stemmers.SnowballStemmer;
//import weka.core.stopwords.StopwordsHandler;
//import weka.filters.Filter;
//import weka.filters.unsupervised.attribute.NumericToNominal;
//import weka.filters.unsupervised.attribute.StringToWordVector;
//
///**
// * Created by Miko on 26.05.2016.
// */
//public class WekaTest {
//
//    private Instances instances;
//
//    public WekaTest() {
//        instances = prepareInstances();
//    }
//
//    public void svm() {
//        try {
//            LibSVM classifier = new LibSVM();
//            LibSVMLoader loader = new LibSVMLoader();
//
//            loader.setSource(new File("C:\\Users\\Miko\\train"));
//            Instances instances = loader.getDataSet();
//            NumericToNominal filter = new NumericToNominal();
//            filter.setInputFormat(instances);
//            int[] attributes = {instances.numAttributes() - 1};
//            filter.setAttributeIndicesArray(attributes);
//            instances = Filter.useFilter(instances, filter);
//            classifier.setSVMType(new SelectedTag(LibSVM.SVMTYPE_C_SVC, LibSVM.TAGS_SVMTYPE));
//            classifier.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
//            classifier.buildClassifier(instances);
//
//            LibSVMLoader loader2 = new LibSVMLoader();
//            loader2.setSource(new File("C:\\Users\\Miko\\test"));
//            Instances instances2 = loader2.getDataSet();
//            instances2 = Filter.useFilter(instances2, filter);
//            double prediction = classifier.classifyInstance(instances2.instance(2));
//            System.out.println("Prediction: " + prediction);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    public void filter(Instances data) {
//        try {
//
//
//            StringToWordVector filter = new StringToWordVector();
//            filter.setInputFormat(data);
//            filter.setLowerCaseTokens(true);
//
//            SnowballStemmer stemmer = new SnowballStemmer();
//            filter.setStopwordsHandler(new Stop());
//            filter.setStemmer(stemmer);
//
//
//
//            Instances newData = Filter.useFilter(data, filter);
//            System.out.println(newData);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    public void addInstanceValue(String text, double label) {
//
//    }
//
//    public Instances getInstances() {
//        return instances;
//    }
//}
