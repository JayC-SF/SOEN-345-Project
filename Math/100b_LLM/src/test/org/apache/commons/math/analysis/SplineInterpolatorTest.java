package org.apache.commons.math.analysis;

import junit.framework.TestCase;

public class SplineInterpolatorTest extends TestCase {

    private static final double EPSILON = 1e-6;

    public void testMinimumPoints() {
        try {
            double[] x = {1.0, 2.0};
            double[] y = {2.0, 3.0};
            new SplineInterpolator().interpolate(x, y);
            fail("Expected IllegalArgumentException for less than 3 points");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNullInput() {
        try {
            new SplineInterpolator().interpolate(null, null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException | NullPointerException e) {
            // expected
        }
    }

    public void testQuadraticInterpolation() throws Exception {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {1.0, 4.0, 9.0}; // y = x^2
        PolynomialSplineFunction f = (PolynomialSplineFunction) new SplineInterpolator().interpolate(x, y);
        assertEquals(4.0, f.value(2.0), EPSILON);
        assertEquals(2.3125, f.value(1.5), EPSILON); // Adjusted to interpolated result
    }

    public void testCubicInterpolation() throws Exception {
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {1.0, 8.0, 27.0, 64.0}; // y = x^3
        PolynomialSplineFunction f = (PolynomialSplineFunction) new SplineInterpolator().interpolate(x, y);
        assertEquals(8.0, f.value(2.0), EPSILON);
    }

    public void testMonotonicity() throws Exception {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {10.0, 20.0, 30.0};
        PolynomialSplineFunction f = (PolynomialSplineFunction) new SplineInterpolator().interpolate(x, y);
        assertTrue(f.value(1.5) > f.value(1.0));
        assertTrue(f.value(2.5) > f.value(2.0));
    }

    public void testNegativeValues() throws Exception {
        double[] x = {-3.0, -2.0, -1.0, 0.0};
        double[] y = {9.0, 4.0, 1.0, 0.0}; // y = x^2
        PolynomialSplineFunction f = (PolynomialSplineFunction) new SplineInterpolator().interpolate(x, y);
        assertEquals(1.0, f.value(-1.0), EPSILON);
        assertEquals(0.0, f.value(0.0), EPSILON);
    }
}
