package pl.edu.pw.elka.sag.weka;

import pl.edu.pw.elka.sag.config.WekaConfig;
import weka.classifiers.Classifier;
import weka.core.SerializationHelper;
import weka.filters.Filter;

import java.io.*;

/**
 * Created by Miko on 28.05.2016.
 */
public class WekaFileHandler {
    public void saveSvmModel(Classifier classifier) throws Exception {
        save(WekaConfig.svmModelFile(), classifier);
    }

    public void saveNaiveBayesModel(Classifier classifier) throws Exception {
        save(WekaConfig.naiveBayesModelFile(), classifier);
    }

    public void saveFilter(Filter filter) throws Exception {
        save(WekaConfig.filterFile(), filter);
    }

    public Classifier loadSvmModel() throws Exception {
        return (Classifier) load(WekaConfig.svmModelFile());
    }

    public Classifier loadNaiveBayesModel() throws Exception {
        return (Classifier) load(WekaConfig.naiveBayesModelFile());
    }

    public Filter loadFilter() throws Exception {
        return (Filter) load(WekaConfig.filterFile());
    }

    private void save(String path, Object o) throws Exception {
        File file = new File(path);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            SerializationHelper.write(fileOutputStream, o);
        }

    }

    private Object load(String path) throws Exception {
        try(FileInputStream fileInputStream = new FileInputStream(new File(path))) {
            return SerializationHelper.read(fileInputStream);
        }
    }
}

