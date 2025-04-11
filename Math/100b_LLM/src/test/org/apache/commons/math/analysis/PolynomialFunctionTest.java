package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import junit.framework.TestCase;

/**
 * Tests the PolynomialFunction implementation of a UnivariateRealFunction.
 *
 * @version $Revision$
 * @author Matt Cliff <matt@mattcliff.com>
 */
public final class PolynomialFunctionTest extends TestCase {

    private static final double EPSILON = 1e-9;

    public void testConstantFunction() throws MathException {
        PolynomialFunction pf = new PolynomialFunction(new double[] { 5.0 });
        assertEquals(5.0, pf.value(0.0), EPSILON);
        assertEquals(5.0, pf.value(-1.0), EPSILON);
        assertEquals(5.0, pf.value(100.0), EPSILON);
    }

    public void testLinearFunction() throws MathException {
        PolynomialFunction pf = new PolynomialFunction(new double[] { 1.0, 2.0 }); // f(x) = 1 + 2x
        assertEquals(1.0, pf.value(0.0), EPSILON);
        assertEquals(3.0, pf.value(1.0), EPSILON);
        assertEquals(-1.0, pf.value(-1.0), EPSILON);
    }

    public void testQuadraticFunction() throws MathException {
        PolynomialFunction pf = new PolynomialFunction(new double[] { 1.0, 0.0, 3.0 }); // f(x) = 1 + 3x^2
        assertEquals(1.0, pf.value(0.0), EPSILON);
        assertEquals(4.0, pf.value(1.0), EPSILON);
        assertEquals(13.0, pf.value(2.0), EPSILON);
    }

    public void testDerivative() throws MathException {
        PolynomialFunction pf = new PolynomialFunction(new double[] { 1.0, 2.0, 3.0 }); // f(x) = 1 + 2x + 3x^2
        PolynomialFunction d = pf.polynomialDerivative(); // f'(x) = 2 + 6x

        assertEquals(2.0, d.value(0.0), EPSILON);
        assertEquals(8.0, d.value(1.0), EPSILON);
        assertEquals(-4.0, d.value(-1.0), EPSILON);
    }

    public void testDegree() {
        PolynomialFunction pf = new PolynomialFunction(new double[] { 0.0, 0.0, 5.0 });
        assertEquals(2, pf.degree());
    }

    public void testCoefficients() {
        double[] coeffs = { 1.0, -2.0, 3.0 };
        PolynomialFunction pf = new PolynomialFunction(coeffs);
        double[] returned = pf.getCoefficients();

        assertEquals(coeffs.length, returned.length);
        for (int i = 0; i < coeffs.length; i++) {
            assertEquals(coeffs[i], returned[i], EPSILON);
        }
    }

    public void testZeroPolynomial() throws MathException {
        PolynomialFunction pf = new PolynomialFunction(new double[] { 0.0 });
        assertEquals(0.0, pf.value(-10.0), EPSILON);
        assertEquals(0.0, pf.value(0.0), EPSILON);
        assertEquals(0.0, pf.value(10.0), EPSILON);
    }
}
