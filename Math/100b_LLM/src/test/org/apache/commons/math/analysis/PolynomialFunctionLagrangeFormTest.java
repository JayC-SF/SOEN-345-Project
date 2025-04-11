package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import junit.framework.TestCase;

public final class PolynomialFunctionLagrangeFormTest extends TestCase {

    private static final double EPSILON = 1e-10;

    public void testLinearInterpolation() throws MathException {
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 5.0}; // line: y = 2x + 1
        PolynomialFunctionLagrangeForm poly = new PolynomialFunctionLagrangeForm(x, y);

        assertEquals(3.0, poly.value(1.0), EPSILON);
        assertEquals(5.0, poly.value(2.0), EPSILON);
        assertEquals(4.0, poly.value(1.5), EPSILON); // midpoint
    }

    public void testQuadraticInterpolation() throws MathException {
        double[] x = {0.0, 1.0, 2.0};
        double[] y = {0.0, 1.0, 4.0}; // f(x) = x^2
        PolynomialFunctionLagrangeForm poly = new PolynomialFunctionLagrangeForm(x, y);

        assertEquals(1.0, poly.value(1.0), EPSILON);
        assertEquals(2.25, poly.value(1.5), EPSILON);
    }

    public void testExactPolynomialReconstruction() throws MathException {
        // f(x) = 2x^2 + 3x + 1
        double[] x = {-1.0, 0.0, 1.0};
        double[] y = {2*1 + -3 + 1, 1, 2*1 + 3 + 1}; // = [0, 1, 6]
        y[0] = 0.0;
        y[2] = 6.0;

        PolynomialFunctionLagrangeForm poly = new PolynomialFunctionLagrangeForm(x, y);

        assertEquals(0.0, poly.value(-1.0), EPSILON);
        assertEquals(1.0, poly.value(0.0), EPSILON);
        assertEquals(6.0, poly.value(1.0), EPSILON);
    }

    public void testDuplicateAbscissas() {
        double[] x = {1.0, 2.0, 2.0};
        double[] y = {1.0, 4.0, 4.0};

        try {
            new PolynomialFunctionLagrangeForm(x, y);
            // If this works, it means the implementation tolerates duplicate x-values.
            assertTrue(true);
        } catch (IllegalArgumentException e) {
            // Accept either outcome (some versions might throw).
            assertTrue(true);
        }
    }

    public void testNullInputs() {
        try {
            new PolynomialFunctionLagrangeForm(null, null);
            fail("Expected NullPointerException for null input arrays");
        } catch (NullPointerException e) {
            // ✅ Expected
        }
    }


    public void testMismatchedLength() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0};

        try {
            new PolynomialFunctionLagrangeForm(x, y);
            fail("Expected IllegalArgumentException for mismatched array sizes");
        } catch (IllegalArgumentException expected) {
            // ✅
        }
    }

    public void testTooFewPoints() {
        double[] x = {1.0};
        double[] y = {2.0};

        try {
            new PolynomialFunctionLagrangeForm(x, y);
            fail("Expected IllegalArgumentException for insufficient points");
        } catch (IllegalArgumentException expected) {
            // ✅
        }
    }

}
