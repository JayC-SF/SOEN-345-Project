package org.apache.commons.math.analysis;

import junit.framework.TestCase;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class TrapezoidIntegratorTest extends TestCase {

    public void testLinearFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x + 1;
            }
        };
        TrapezoidIntegrator integrator = new TrapezoidIntegrator(f);
        try {
            double result = integrator.integrate(0, 1);
            assertEquals(2.0, result, 1E-6); // ∫(2x+1) from 0 to 1 = 2
        } catch (FunctionEvaluationException | MaxIterationsExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testQuadraticFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        TrapezoidIntegrator integrator = new TrapezoidIntegrator(f);
        try {
            double result = integrator.integrate(0, 1);
            assertEquals(1.0 / 3.0, result, 1E-6); // ∫x^2 from 0 to 1 = 1/3
        } catch (FunctionEvaluationException | MaxIterationsExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testSineFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        TrapezoidIntegrator integrator = new TrapezoidIntegrator(f);
        try {
            double result = integrator.integrate(0, Math.PI);
            assertEquals(2.0, result, 1E-6); // ∫sin(x) from 0 to π = 2
        } catch (FunctionEvaluationException | MaxIterationsExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testLogFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.log(x);
            }
        };
        TrapezoidIntegrator integrator = new TrapezoidIntegrator(f);
        try {
            double result = integrator.integrate(1, Math.E);
            assertEquals(1.0, result, 1E-6); // ∫ln(x) from 1 to e = 1
        } catch (FunctionEvaluationException | MaxIterationsExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    public void testExponentialFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.exp(x);
            }
        };
        TrapezoidIntegrator integrator = new TrapezoidIntegrator(f);
        try {
            double result = integrator.integrate(0, 1);
            assertEquals(Math.E - 1.0, result, 1E-6); // ∫e^x from 0 to 1 = e - 1
        } catch (FunctionEvaluationException | MaxIterationsExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
