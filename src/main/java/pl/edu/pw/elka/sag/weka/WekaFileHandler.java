package pl.edu.pw.elka.sag.weka;

import pl.edu.pw.elka.sag.config.WekaConfig;
import weka.classifiers.Classifier;
import weka.core.SerializationHelper;
import weka.filters.Filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Miko on 28.05.2016.
 */
public class WekaFileHandler {
    public void saveClassifier(Classifier classifier, ClsType classifierType) throws Exception {
        String path = getModelPath(classifierType);
        save(path, classifier);
    }

    public void saveFilter(Filter filter, ClsType classifierType) throws Exception {
        String path = getFilterPath(classifierType);
        save(path, filter);
    }

    public Classifier loadClassifier(ClsType classifierType) throws Exception {
        String path = getModelPath(classifierType);
        return (Classifier) load(path);
    }

    public Filter loadFilter(ClsType classifierType) throws Exception {
        String path = getFilterPath(classifierType);
        return (Filter) load(path);
    }

    private void save(String path, Object o) throws Exception {
        File file = new File(path);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            SerializationHelper.write(fileOutputStream, o);
        }

    }

    private Object load(String path) throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream(new File(path))) {
            return SerializationHelper.read(fileInputStream);
        }
    }

    private String getModelPath(ClsType clsType) {
        String path = null;
        switch (clsType) {
            case SVM:
                path = WekaConfig.svmModelFile();
                break;
            case NB:
                path = WekaConfig.naiveBayesModelFile();
                break;
        }

        return path;
    }

    private String getFilterPath(ClsType clsType) {
        String path = null;
        switch (clsType) {
            case SVM:
                path = WekaConfig.svmFilterFile();
                break;
            case NB:
                path = WekaConfig.naiveBayesFilterFile();
                break;
        }

        return path;
    }
}

