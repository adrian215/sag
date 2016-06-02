package pl.edu.pw.elka.sag.weka.optimize;

import weka.classifiers.Classifier;
import weka.classifiers.meta.MultiSearch;
import weka.classifiers.meta.multisearch.DefaultEvaluationMetrics;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Utils;
import weka.core.setupgenerator.AbstractParameter;
import weka.core.setupgenerator.MathParameter;

class SearchBuilder {

    private MultiSearch multiSearch = initSearch();

    public SearchBuilder(Classifier classifier) {
        multiSearch.setClassifier(classifier);
    }

    public static SearchBuilder createFromClassifier(Classifier classifier) {
        return new SearchBuilder(classifier);
    }

    private MultiSearch initSearch() {
        MultiSearch multiSearch = new MultiSearch();
        multiSearch.setDebug(true);
        return  multiSearch;
    }

    public SearchBuilder selectionStrategy(int evaluationMetrics) {
        SelectedTag tag = new SelectedTag(evaluationMetrics, new DefaultEvaluationMetrics().getTags());
        multiSearch.setEvaluation(tag);
        return this;
    }

    public SearchBuilder withParams(MathParameter params) {
        multiSearch.setSearchParameters(new AbstractParameter[] {params});
        return this;
    }

    public Classifier formData(Instances data) throws Exception {
        multiSearch.buildClassifier(data);
        Classifier bestClassifier = multiSearch.getBestClassifier();
        bestClassifier.buildClassifier(data);

        System.out.println("Zakończono optymalizacje modelu");
        System.out.println("Model: " + Utils.toCommandLine(multiSearch.getBestClassifier()));

        return bestClassifier;
    }

}
