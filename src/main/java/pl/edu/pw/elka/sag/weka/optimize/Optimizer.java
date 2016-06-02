package pl.edu.pw.elka.sag.weka.optimize;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.setupgenerator.AbstractParameter;
import weka.core.setupgenerator.MathParameter;

import static pl.edu.pw.elka.sag.weka.optimize.OptimizationParameterBuilder.optimizeProperty;
import static weka.classifiers.meta.multisearch.DefaultEvaluationMetrics.EVALUATION_ACC;
import static weka.classifiers.meta.multisearch.DefaultEvaluationMetrics.EVALUATION_AUC;

public class Optimizer {

    public static Optimizer create() {
        return new Optimizer();
    }

    public Classifier optimizeClassifier(Instances data, Classifier classifier) throws Exception {

        MathParameter cost =
                optimizeProperty("cost")
                        .from(-5)
                        .to(5)
                        .withStep(1)
                        .get();

        MathParameter gamma =
                optimizeProperty("gamma")
                        .from(-11)
                        .to(-3)
                        .withStep(1)
                        .withBase(2)
                        .get();

        AbstractParameter[] optimizationParams = {cost, gamma};

        return optimizeClassifier(classifier)
                .selectionStrategy(EVALUATION_ACC)
                .withParams(optimizationParams)
                .formData(data);

    }

    private SearchBuilder optimizeClassifier(Classifier classifier) {
        return SearchBuilder.createSearchFromClassifier(classifier);
    }
}
