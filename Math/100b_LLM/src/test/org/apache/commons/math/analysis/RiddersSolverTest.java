package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import junit.framework.TestCase;

public final class RiddersSolverTest extends TestCase {

    private static final double TOLERANCE = 1e-6;

    public void testLinearFunctionRoot() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 6;
            }
        };
        RiddersSolver solver = new RiddersSolver(f);
        double result = solver.solve(0, 10);
        assertEquals(3.0, result, TOLERANCE);
    }

    public void testRootAtEndpoint() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        RiddersSolver solver = new RiddersSolver(f);
        double result = solver.solve(0, 1);
        assertEquals(0.0, result, TOLERANCE);
    }

    public void testNearZeroRoot() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1e-8;
            }
        };
        RiddersSolver solver = new RiddersSolver(f);
        double result = solver.solve(0, 1e-7);
        assertEquals(1e-8, result, TOLERANCE);
    }

    public void testInvalidInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x + 1; // always positive, no root
            }
        };

        try {
            UnivariateRealSolver solver = new RiddersSolver(f);
            solver.solve(-2, 2);
            fail("Expected IllegalArgumentException for invalid interval");
        } catch (IllegalArgumentException e) {
            // expected
        } catch (MathException e) {
            fail("Expected IllegalArgumentException, but got MathException");
        }
    }


    public void testNullFunction() {
        try {
            new RiddersSolver(null);
            fail("Expected IllegalArgumentException for null function");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNoSignChange() {
        try {
            UnivariateRealFunction f = new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x + 1; // no real root
                }
            };
            RiddersSolver solver = new RiddersSolver(f);
            solver.solve(-2, 2);
            fail("Expected IllegalArgumentException due to no sign change");
        } catch (IllegalArgumentException e) {
            // expected
        } catch (MathException e) {
            fail("Did not expect MathException");
        }
    }
}
