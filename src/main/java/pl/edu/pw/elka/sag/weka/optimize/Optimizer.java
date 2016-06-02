package pl.edu.pw.elka.sag.weka.optimize;

import pl.edu.pw.elka.sag.config.Configuration;
import pl.edu.pw.elka.sag.config.WekaConfig;
import pl.edu.pw.elka.sag.weka.ClsType;
import scala.concurrent.java8.FuturesConvertersImpl;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.setupgenerator.AbstractParameter;
import weka.core.setupgenerator.MathParameter;

import java.util.ArrayList;

import static pl.edu.pw.elka.sag.weka.optimize.OptimizationParameterBuilder.optimizeProperty;
import static weka.classifiers.meta.multisearch.DefaultEvaluationMetrics.EVALUATION_ACC;
import static weka.classifiers.meta.multisearch.DefaultEvaluationMetrics.EVALUATION_AUC;

public class Optimizer {

    public static Optimizer create() {
        return new Optimizer();
    }

    public Classifier optimizeClassifier(Instances data, Classifier classifier, ClsType clsType) throws Exception {
        WekaConfig wekaConfig = Configuration.getConfig();
        MathParameter cost =
                optimizeProperty("cost")
                        .from(wekaConfig.start())
                        .to(wekaConfig.end())
                        .withStep(wekaConfig.step())
                        .withBase(wekaConfig.base())
                        .get();

        MathParameter gamma =
                optimizeProperty("gamma")
                        .from(wekaConfig.startNl())
                        .to(wekaConfig.endNl())
                        .withStep(wekaConfig.stepNl())
                        .withBase(wekaConfig.baseNl())
                        .get();

        ArrayList<AbstractParameter> optimizationParams = new ArrayList<>();
        optimizationParams.add(cost);

        if(clsType == ClsType.SVMNL) {
            optimizationParams.add(gamma);
        }

        return optimizeClassifier(classifier)
                .selectionStrategy(EVALUATION_ACC)
                .withParams(optimizationParams.toArray(new AbstractParameter[0]))
                .formData(data);

    }

    private SearchBuilder optimizeClassifier(Classifier classifier) {
        return SearchBuilder.createSearchFromClassifier(classifier);
    }
}
