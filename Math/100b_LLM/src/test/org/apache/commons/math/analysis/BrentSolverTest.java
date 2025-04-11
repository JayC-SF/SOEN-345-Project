package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public final class BrentSolverTest extends TestCase {

    public static Test suite() {
        return new TestSuite(BrentSolverTest.class);
    }

    public void testSimpleRoot() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
        };
        BrentSolver solver = new BrentSolver(f);
        double result = solver.solve(1, 5, 2);
        assertEquals(3.0, result, 1e-6);
    }

    public void testRootAtBoundary() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
        };
        BrentSolver solver = new BrentSolver(f);
        double result = solver.solve(0, 2, 1);
        assertEquals(0.0, result, 1e-6);
    }

    public void testTinyRoot() throws MathException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1e-9;
            }
        };
        BrentSolver solver = new BrentSolver(f);
        double result = solver.solve(0, 1e-8, 1e-9);
        assertEquals(1e-9, result, 1e-8);
    }

    public void testBadFunctionThrowsArithmeticException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new ArithmeticException("Exploding function");
            }
        };
        try {
            BrentSolver solver = new BrentSolver(f);
            solver.solve(0, 1, 0.5);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        } catch (MathException e) {
            fail("Did not expect MathException");
        }
    }

}
