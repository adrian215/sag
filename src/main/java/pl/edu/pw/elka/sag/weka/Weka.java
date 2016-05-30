package pl.edu.pw.elka.sag.weka;

import pl.edu.pw.elka.sag.config.Configuration;
import pl.edu.pw.elka.sag.config.WekaConfig;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.stemmers.IteratedLovinsStemmer;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Miko on 27.05.2016.
 */
public class Weka {
    private WekaConfig wekaConfig = Configuration.getConfig();

    public Weka() {
//        stopwordsHandler = new MyStopWordHandler();
    }

    public Instances prepareInstances() {
        ArrayList<Attribute> atts = new ArrayList<>();

        atts.add(new Attribute("tweet", (ArrayList<String>) null));
        atts.add(new Attribute("sentiment", Arrays.asList("negative", "positive")));

        return new Instances("Rel", atts, 0);
    }

    public Filter createFilter(Instances data) throws Exception {
        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(data);
        filter.setLowerCaseTokens(true);
        filter.setStemmer(new IteratedLovinsStemmer());
        filter.setTokenizer(new AlphabeticTokenizer());
        filter.setWordsToKeep(wekaConfig.wordsToKeep());

        return filter;
    }

    public Instances filter(Instances data, Filter filter) throws Exception {
        data = removeNull(data);
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
            case NB:
                classifier = getNaiveBayes();
                break;
        }

        data.randomize(new Random());
        int trainingSize = Math.round(wekaConfig.trainingSetSize() * data.size());
        int testSize = data.size() - trainingSize;
        Instances training = new Instances(data, 0, trainingSize);
        Instances test = new Instances(data, trainingSize, testSize);

        System.out.println(training.toSummaryString());
        System.out.println(test.toSummaryString());

        classifier.buildClassifier(training);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(classifier, test);
        System.out.println(eval.toSummaryString(true));
        return classifier;
    }

    private Classifier getSVM() {
        LibSVM classifier = new LibSVM();
        classifier.setSVMType(new SelectedTag(LibSVM.SVMTYPE_C_SVC, LibSVM.TAGS_SVMTYPE));
        classifier.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
        return classifier;
    }

    private Classifier getNaiveBayes() {
        NaiveBayes classifier = new NaiveBayes();
        return classifier;
    }

    private Instances removeNull(Instances instances) {
        Instances notNullInstances = new Instances(instances, 0);
        for (int i =0; i < instances.size(); i++) {
            try {
                instances.get(i).toString();
                notNullInstances.add(instances.get(i));
            }
            catch (NullPointerException e) {

            }
        }
        return notNullInstances;
    }
}
