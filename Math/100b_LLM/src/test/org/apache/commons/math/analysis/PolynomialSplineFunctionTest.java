package org.apache.commons.math.analysis;

import java.util.Arrays;
import junit.framework.TestCase;
import org.apache.commons.math.FunctionEvaluationException;

public class PolynomialSplineFunctionTest extends TestCase {

    private static final double EPSILON = 1e-9;

    public void testSplineEvaluation() throws FunctionEvaluationException {
        double[] knots = {0.0, 1.0, 2.0};
        PolynomialFunction[] polynomials = {
                new PolynomialFunction(new double[] {1.0}),
                new PolynomialFunction(new double[] {2.0})
        };
        PolynomialSplineFunction spline = new PolynomialSplineFunction(knots, polynomials);

        assertEquals(1.0, spline.value(0.5), 1e-10);
        assertEquals(2.0, spline.value(1.5), 1e-10);
        assertEquals(2.0, spline.value(2.0), 1e-10); // end of domain
    }



    public void testIllegalValueBelowRange() {
        double[] knots = {0.0, 1.0, 2.0};
        PolynomialFunction[] polynomials = {
                new PolynomialFunction(new double[] {1.0}),
                new PolynomialFunction(new double[] {0.0, 1.0})
        };
        PolynomialSplineFunction spline = new PolynomialSplineFunction(knots, polynomials);

        try {
            spline.value(-0.1);
            fail("Expected IllegalArgumentException for input below range");
        } catch (IllegalArgumentException | FunctionEvaluationException e) {
            // Expected
        }
    }

    public void testIllegalValueAboveRange() {
        double[] knots = {0.0, 1.0, 2.0};
        PolynomialFunction[] polynomials = {
                new PolynomialFunction(new double[] {1.0}),
                new PolynomialFunction(new double[] {0.0, 1.0})
        };
        PolynomialSplineFunction spline = new PolynomialSplineFunction(knots, polynomials);

        try {
            spline.value(2.1);
            fail("Expected IllegalArgumentException for input above range");
        } catch (IllegalArgumentException | FunctionEvaluationException e) {
            // Expected
        }
    }

    public void testKnotsAndPolynomialsRetrieval() {
        double[] knots = {0.0, 1.0, 2.0};
        PolynomialFunction[] polynomials = {
                new PolynomialFunction(new double[] {1.0}),
                new PolynomialFunction(new double[] {2.0})
        };
        PolynomialSplineFunction spline = new PolynomialSplineFunction(knots, polynomials);

        assertTrue(Arrays.equals(knots, spline.getKnots()));
        assertEquals(2, spline.getN());
        assertEquals(2, spline.getPolynomials().length);
        assertEquals(1.0, spline.getPolynomials()[0].value(0.0), EPSILON);
        assertEquals(2.0, spline.getPolynomials()[1].value(0.0), EPSILON);
    }

    public void testIllegalConstructorArguments() {
        try {
            new PolynomialSplineFunction(null, null);
            fail("Expected IllegalArgumentException for null inputs");
        } catch (IllegalArgumentException e) {
            // expected
        } catch (NullPointerException e) {
            // acceptable fallback for older versions
        }
    }

}
