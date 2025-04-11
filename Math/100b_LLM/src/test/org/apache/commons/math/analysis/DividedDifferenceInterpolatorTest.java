package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import junit.framework.TestCase;

public final class DividedDifferenceInterpolatorTest extends TestCase {

    public void testLinearInterpolation() throws MathException {
        DividedDifferenceInterpolator interpolator = new DividedDifferenceInterpolator();
        double[] x = {1.0, 2.0};
        double[] y = {2.0, 4.0};
        UnivariateRealFunction f = interpolator.interpolate(x, y);
        assertEquals(3.0, f.value(1.5), 1e-6);
    }

    public void testQuadraticInterpolation() throws MathException {
        DividedDifferenceInterpolator interpolator = new DividedDifferenceInterpolator();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.0, 4.0, 9.0}; // y = x^2
        UnivariateRealFunction f = interpolator.interpolate(x, y);
        assertEquals(2.25, f.value(1.5), 1e-6);
    }

    public void testRepeatedXThrowsException() {
        DividedDifferenceInterpolator interpolator = new DividedDifferenceInterpolator();
        double[] x = {1.0, 2.0, 2.0};
        double[] y = {1.0, 4.0, 4.0};
        try {
            interpolator.interpolate(x, y);
            fail("Expected MathException for repeated x-values");
        } catch (MathException expected) {
            // passed
        }
    }

    public void testInterpolationEvaluation() throws MathException {
        DividedDifferenceInterpolator interpolator = new DividedDifferenceInterpolator();
        double[] x = {0.0, 1.0, 2.0};
        double[] y = {0.0, 1.0, 4.0}; // f(x) = x^2
        UnivariateRealFunction f = interpolator.interpolate(x, y);
        assertEquals(0.0, f.value(0.0), 1e-6);
        assertEquals(1.0, f.value(1.0), 1e-6);
        assertEquals(4.0, f.value(2.0), 1e-6);
    }

    public void testNullInput() {
        DividedDifferenceInterpolator interpolator = new DividedDifferenceInterpolator();
        try {
            interpolator.interpolate(null, null);
            fail("Expected exception");
        } catch (NullPointerException expected) {
            // expected
        } catch (MathException e) {
            fail("Did not expect MathException");
        }
    }
}
