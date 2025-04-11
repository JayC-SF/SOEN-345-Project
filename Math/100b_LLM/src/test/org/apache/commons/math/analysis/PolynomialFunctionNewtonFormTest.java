package org.apache.commons.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;
import junit.framework.TestCase;

public final class PolynomialFunctionNewtonFormTest extends TestCase {

    private static final double EPSILON = 1e-10;

    public void testLinearFunction() throws FunctionEvaluationException {
        double[] a = {3.0, 2.0};
        double[] c = {0.0};
        PolynomialFunctionNewtonForm poly = new PolynomialFunctionNewtonForm(a, c);

        assertEquals(3.0, poly.value(0.0), EPSILON);
        assertEquals(5.0, poly.value(1.0), EPSILON);
        assertEquals(7.0, poly.value(2.0), EPSILON);
    }

    public void testQuadraticFunction() throws FunctionEvaluationException {
        double[] a = {0.0, 1.0, 1.0};
        double[] c = {0.0, 1.0};
        PolynomialFunctionNewtonForm poly = new PolynomialFunctionNewtonForm(a, c);

        assertEquals(0.0, poly.value(0.0), EPSILON);
        assertEquals(1.0, poly.value(1.0), EPSILON);
        assertEquals(4.0, poly.value(2.0), EPSILON);
    }

    public void testZeroPolynomial() throws FunctionEvaluationException {
        double[] a = {0.0, 0.0};
        double[] c = {1.0};  // Any x-value is fine, since coefficients are zero
        PolynomialFunctionNewtonForm poly = new PolynomialFunctionNewtonForm(a, c);

        assertEquals(0.0, poly.value(-100), EPSILON);
        assertEquals(0.0, poly.value(0), EPSILON);
        assertEquals(0.0, poly.value(100), EPSILON);
    }

    public void testMismatchLength() {
        double[] a = {1.0, 2.0};
        double[] c = {};
        try {
            new PolynomialFunctionNewtonForm(a, c);
            fail("Expected IllegalArgumentException for mismatched lengths");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNullInputs() {
        try {
            new PolynomialFunctionNewtonForm(null, null);
            fail("Expected NullPointerException for null input arrays");
        } catch (NullPointerException e) {
            // Expected
        }
    }


    public void testHighDegreePolynomial() throws FunctionEvaluationException {
        double[] a = {0.0, 1.0, 1.0, 1.0};
        double[] c = {0.0, 1.0, 2.0};
        PolynomialFunctionNewtonForm poly = new PolynomialFunctionNewtonForm(a, c);

        assertEquals(0.0, poly.value(0.0), EPSILON);
        assertEquals(1.0, poly.value(1.0), EPSILON);
        assertEquals(4.0, poly.value(2.0), EPSILON);
        assertEquals(15.0, poly.value(3.0), EPSILON);
    }

    public void testEvaluateAtCenterPoints() throws FunctionEvaluationException {
        double[] a = {1.0, -2.0, 3.0};
        double[] c = {2.0, 3.0};
        PolynomialFunctionNewtonForm poly = new PolynomialFunctionNewtonForm(a, c);

        assertEquals(1.0, poly.value(2.0), EPSILON);
        assertEquals(-1.0, poly.value(3.0), EPSILON);
    }
}
