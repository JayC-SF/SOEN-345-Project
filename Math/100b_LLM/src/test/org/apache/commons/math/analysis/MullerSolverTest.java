package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import junit.framework.TestCase;

public final class MullerSolverTest extends TestCase {

    private static final double EPSILON = 1e-6;

    public void testLinearRoot() throws MathException {
        PolynomialFunction f = new PolynomialFunction(new double[]{-5.0, 1.0}); // x - 5
        MullerSolver solver = new MullerSolver(f);
        double root = solver.solve(0.0, 10.0, 1.0);
        assertEquals(5.0, root, EPSILON);
    }

    public void testQuadraticRoot() throws MathException {
        PolynomialFunction f = new PolynomialFunction(new double[]{-4.0, 0.0, 1.0}); // x^2 - 4
        MullerSolver solver = new MullerSolver(f);
        double root = solver.solve(0.0, 5.0, 2.5);  // root near 2
        assertEquals(2.0, root, EPSILON);
    }

    public void testRootAtZero() throws MathException {
        PolynomialFunction f = new PolynomialFunction(new double[]{0.0, 1.0}); // x
        MullerSolver solver = new MullerSolver(f);
        double root = solver.solve(-1.0, 1.0, 0.0);
        assertEquals(0.0, root, EPSILON);
    }

    public void testInvalidInterval() {
        PolynomialFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 1.0}); // x^2 + 1
        MullerSolver solver = new MullerSolver(f);
        try {
            solver.solve(-5.0, 5.0, 0.0);
            fail("Expected IllegalArgumentException due to no real root (no sign change)");
        } catch (IllegalArgumentException expected) {
            // ✅ Expected
        } catch (MathException e) {
            fail("Unexpected MathException instead of IllegalArgumentException");
        }
    }

    public void testNullFunction() {
        try {
            new MullerSolver(null);
            fail("Expected IllegalArgumentException for null function");
        } catch (IllegalArgumentException expected) {
            // ✅ Expected
        }
    }
}
