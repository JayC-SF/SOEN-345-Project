package org.apache.commons.math.analysis;

import junit.framework.TestCase;
import org.apache.commons.math.MathException;

/**
 * Testcase for Simpson integrator.
 */
public final class SimpsonIntegratorTest extends TestCase {

    private static final double EPSILON = 1e-6;

    public void testLinearFunction() throws MathException {
        // f(x) = 2x + 1 over [0, 1] ⇒ integral = 2.0
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x + 1;
            }
        };
        SimpsonIntegrator integrator = new SimpsonIntegrator(f);
        double result = integrator.integrate(0.0, 1.0);
        assertEquals(2.0, result, EPSILON);
    }

    public void testQuadraticFunction() throws MathException {
        // f(x) = x² over [0, 1] ⇒ integral = 1/3
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        SimpsonIntegrator integrator = new SimpsonIntegrator(f);
        double result = integrator.integrate(0.0, 1.0);
        assertEquals(1.0 / 3.0, result, EPSILON);
    }

    public void testSineFunction() throws MathException {
        // f(x) = sin(x) over [0, π] ⇒ integral = 2
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        SimpsonIntegrator integrator = new SimpsonIntegrator(f);
        double result = integrator.integrate(0.0, Math.PI);
        assertEquals(2.0, result, EPSILON);
    }

    public void testExponentialFunction() throws MathException {
        // f(x) = e^x over [0, 1] ⇒ integral = e - 1
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.exp(x);
            }
        };
        SimpsonIntegrator integrator = new SimpsonIntegrator(f);
        double result = integrator.integrate(0.0, 1.0);
        assertEquals(Math.E - 1.0, result, EPSILON);
    }

    public void testConstantFunction() throws MathException {
        // f(x) = 5 over [0, 1] ⇒ integral = 5
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 5.0;
            }
        };
        SimpsonIntegrator integrator = new SimpsonIntegrator(f);
        double result = integrator.integrate(0.0, 1.0);
        assertEquals(5.0, result, EPSILON);
    }

    public void testInvalidInterval() {
        try {
            UnivariateRealFunction f = new UnivariateRealFunction() {
                public double value(double x) {
                    return x;
                }
            };
            SimpsonIntegrator integrator = new SimpsonIntegrator(f);
            integrator.integrate(1.0, 0.0); // Invalid interval
            fail("Expected IllegalArgumentException for invalid bounds");
        } catch (IllegalArgumentException e) {
            // expected
        } catch (MathException e) {
            fail("Did not expect MathException");
        }
    }
}