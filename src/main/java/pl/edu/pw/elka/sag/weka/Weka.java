package pl.edu.pw.elka.sag.weka;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SerializationHelper;
import weka.core.stemmers.IteratedLovinsStemmer;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.StopwordsHandler;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Miko on 27.05.2016.
 */
public class Weka {
    private StopwordsHandler stopwordsHandler;

    public Weka() {
        stopwordsHandler = new MyStopWordHandler();
    }

    public Instances prepareInstances() {
        ArrayList<Attribute> atts = new ArrayList<>();

        atts.add(new Attribute("tweet", (ArrayList<String>) null));
        atts.add(new Attribute("sentiment", Arrays.asList("negative", "positive")));

        return new Instances("Rel", atts, 0);
    }

    public Instances filter(Instances data) throws Exception {
        StringToWordVector filter = new StringToWordVector();

        filter.setInputFormat(data);
        filter.setLowerCaseTokens(true);
        filter.setStemmer(new IteratedLovinsStemmer());
        //filter.setStopwordsHandler(stopwordsHandler);
        filter.setTokenizer(new AlphabeticTokenizer());
        Instances filteredData = Filter.useFilter(data, filter);
        filteredData.setClassIndex(0);
        return filteredData;
    }

    public Classifier buildModel(Instances data, ClsType clsType) throws Exception {
        Classifier classifier = null;
        switch (clsType) {
            case SVM:
                classifier = getSVM();
                break;
            case NAIVE_BAYES:
                classifier = getNaiveBayes();
                break;
        }

        Evaluation eval = new Evaluation(data);
        eval.crossValidateModel(classifier, data, 10, new Random());
        System.out.println(eval.toSummaryString(true));
        saveModel("svm.model", classifier);
        return classifier;
    }

    public void saveModel(String filePath, Classifier classifier) throws Exception {
        SerializationHelper.write(filePath, classifier);
    }

    private Classifier getSVM() {
        LibSVM classifier = new LibSVM();
        classifier.setSVMType(new SelectedTag(LibSVM.SVMTYPE_NU_SVC, LibSVM.TAGS_SVMTYPE));
        classifier.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
        return classifier;
    }

    private Classifier getNaiveBayes() {
        NaiveBayes classifier = new NaiveBayes();
        return classifier;
    }
}
