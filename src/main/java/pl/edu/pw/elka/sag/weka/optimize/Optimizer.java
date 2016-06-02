package pl.edu.pw.elka.sag.weka.optimize;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.setupgenerator.MathParameter;

import static weka.classifiers.meta.multisearch.DefaultEvaluationMetrics.EVALUATION_AUC;

public class Optimizer {

    public static Optimizer create() {
        return new Optimizer();
    }

    public Classifier optimizeClassifier(Instances data, Classifier classifier) throws Exception {

        MathParameter mathParameter = optimizeCost()
                .from(-5)
                .to(5)
                .withStep(1)
                .get();

        return optimizeClassifier(classifier)
                .selectionStrategy(EVALUATION_AUC)
                .withParams(mathParameter)
                .formData(data);

    }

    private SearchBuilder optimizeClassifier(Classifier classifier) {
        return SearchBuilder.createFromClassifier(classifier);
    }

    private CostParameterBuilder optimizeCost() {
        return CostParameterBuilder.create();
    }
}
