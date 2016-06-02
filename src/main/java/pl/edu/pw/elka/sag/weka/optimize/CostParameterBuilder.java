package pl.edu.pw.elka.sag.weka.optimize;

import weka.core.setupgenerator.MathParameter;

public class CostParameterBuilder {

    private MathParameter params = getDefaultParameters();

    public static CostParameterBuilder create() {
        return new CostParameterBuilder();
    }

    private MathParameter getDefaultParameters() {
        MathParameter def = new MathParameter();
        def.setProperty("cost");
        def.setBase(10);
        def.setExpression("pow(BASE,I)");
        return def;
    }

    public CostParameterBuilder from(int min) {
        params.setMin(min);
        return this;
    }

    public CostParameterBuilder to(int max) {
        params.setMax(max);
        return this;
    }

    public CostParameterBuilder withStep(int step) {
        params.setStep(step);
        return this;
    }

    public MathParameter get() {
        return params;
    }
}
