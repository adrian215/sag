package pl.edu.pw.elka.sag.weka.optimize;

import weka.core.setupgenerator.MathParameter;

public class OptimizationParameterBuilder {

    private static final int DEFAULT_BASE = 10;
    private static final int DEFAULT_MIN = 1;
    private static final int DEFAULT_MAX = 10;
    private static final String DEFAULT_EXPRESSION = "pow(BASE,I)";


    private MathParameter params;

    OptimizationParameterBuilder(String property) {
        params = getDefaultParameters(property);
    }

    public static OptimizationParameterBuilder optimizeProperty(String property) {
        return new OptimizationParameterBuilder(property);
    }

    private MathParameter getDefaultParameters(String property) {
        MathParameter def = new MathParameter();
        def.setProperty(property);
        def.setBase(DEFAULT_BASE);
        def.setMin(DEFAULT_MIN);
        def.setMax(DEFAULT_MAX);
        def.setExpression(DEFAULT_EXPRESSION);
        return def;
    }

    public OptimizationParameterBuilder from(int min) {
        params.setMin(min);
        return this;
    }

    public OptimizationParameterBuilder to(int max) {
        params.setMax(max);
        return this;
    }

    public OptimizationParameterBuilder withStep(int step) {
        params.setStep(step);
        return this;
    }

    public OptimizationParameterBuilder withBase(int base) {
        params.setBase(base);
        return this;
    }

    public OptimizationParameterBuilder withExpression(String expression) {
        params.setExpression(expression);
        return this;
    }

    public MathParameter get() {
        return params;
    }
}
