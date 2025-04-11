package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import junit.framework.TestCase;

public final class NevilleInterpolatorTest extends TestCase {

    private static final double EPSILON = 1e-6;

    public void testLinearInterpolation() throws MathException {
        // f(x) = 2x
        double[] x = {0.0, 1.0};
        double[] y = {0.0, 2.0};
        NevilleInterpolator interpolator = new NevilleInterpolator();
        UnivariateRealFunction f = interpolator.interpolate(x, y);

        assertEquals(1.0, f.value(0.5), EPSILON);
        assertEquals(2.0, f.value(1.0), EPSILON);
    }

    public void testQuadraticInterpolation() throws MathException {
        // f(x) = x^2
        double[] x = {0.0, 1.0, 2.0};
        double[] y = {0.0, 1.0, 4.0};
        NevilleInterpolator interpolator = new NevilleInterpolator();
        UnivariateRealFunction f = interpolator.interpolate(x, y);

        assertEquals(0.25, f.value(0.5), EPSILON);  // Should be close to 0.5^2
        assertEquals(2.25, f.value(1.5), EPSILON);  // 1.5^2 = 2.25
    }

    public void testInvalidArrayLengths() {
        double[] x = {0.0, 1.0};
        double[] y = {1.0};

        try {
            NevilleInterpolator interpolator = new NevilleInterpolator();
            interpolator.interpolate(x, y);
            fail("Expected IllegalArgumentException due to mismatched array lengths");
        } catch (IllegalArgumentException expected) {
            // Expected
        } catch (MathException e) {
            fail("Unexpected MathException");
        }
    }

    public void testTooFewPoints() {
        double[] x = {0.0};
        double[] y = {1.0};

        try {
            NevilleInterpolator interpolator = new NevilleInterpolator();
            interpolator.interpolate(x, y);
            fail("Expected IllegalArgumentException for insufficient data points");
        } catch (IllegalArgumentException expected) {
            // Expected
        } catch (MathException e) {
            fail("Unexpected MathException");
        }
    }
}
