package pl.edu.pw.elka.sag.weka;

import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.LibSVMLoader;
import weka.core.stemmers.SnowballStemmer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Miko on 27.05.2016.
 */
public class Weka {

    public Instances prepareInstances() {
        ArrayList<Attribute> atts = new ArrayList<>();

        atts.add(new Attribute("tweet", (ArrayList<String>)null));
        atts.add(new Attribute("sentiment", Arrays.asList("negative", "positive")));

        return new Instances("Rel", atts, 0);
    }

    public void filter(Instances instances) {
        try {
            SnowballStemmer stemmer = new SnowballStemmer();
            StringToWordVector filter = new StringToWordVector();

            filter.setInputFormat(instances);
            filter.setLowerCaseTokens(true);
            filter.setStemmer(stemmer);

            Instances filteredInstances = Filter.useFilter(instances, filter);
            System.out.print(filteredInstances);
            svm(filteredInstances);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void svm(Instances instances) {
        try {
            LibSVM classifier = new LibSVM();

            instances.setClassIndex(0);
            classifier.setSVMType(new SelectedTag(LibSVM.SVMTYPE_C_SVC, LibSVM.TAGS_SVMTYPE));
            classifier.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
            classifier.buildClassifier(instances);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
