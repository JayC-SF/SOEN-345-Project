package org.apache.commons.math.analysis;

import junit.framework.TestCase;
import org.apache.commons.math.MathException;

/**
 * Testcase for Romberg integrator.
 */
public final class RombergIntegratorTest extends TestCase {

    private static final double EPSILON = 1e-6;

    public void testLinearFunction() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2.0 * x + 1.0; // ∫0^1 (2x + 1) dx = 2.0
            }
        };
        RombergIntegrator integrator = new RombergIntegrator(f);
        double result = integrator.integrate(0.0, 1.0);
        assertEquals(2.0, result, EPSILON);
    }

    public void testQuadraticFunction() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // ∫0^1 x² dx = 1/3
            }
        };
        RombergIntegrator integrator = new RombergIntegrator(f);
        double result = integrator.integrate(0.0, 1.0);
        assertEquals(1.0 / 3.0, result, EPSILON);
    }

    public void testExponentialFunction() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.exp(x); // ∫0^1 e^x dx = e - 1
            }
        };
        RombergIntegrator integrator = new RombergIntegrator(f);
        double result = integrator.integrate(0.0, 1.0);
        assertEquals(Math.E - 1.0, result, EPSILON);
    }

    public void testSineFunction() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x); // ∫0^π sin(x) dx = 2
            }
        };
        RombergIntegrator integrator = new RombergIntegrator(f);
        double result = integrator.integrate(0.0, Math.PI);
        assertEquals(2.0, result, EPSILON);
    }
}
