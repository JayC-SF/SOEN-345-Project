
package org.apache.commons.math.analysis;

import junit.framework.TestCase;
import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.PolynomialFunction;


public final class LaguerreSolverTest extends TestCase {

    private static final double EPSILON = 1e-6;

    public void testLinearRoot() throws MathException {
        PolynomialFunction f = new PolynomialFunction(new double[]{-2.0, 1.0}); // x - 2
        LaguerreSolver solver = new LaguerreSolver(f);
        double root = solver.solve(0, 5, 1);
        assertEquals(2.0, root, EPSILON);
    }

    public void testQuadraticRoot() throws MathException {
        PolynomialFunction f = new PolynomialFunction(new double[]{-4.0, 0.0, 1.0}); // x^2 - 4
        LaguerreSolver solver = new LaguerreSolver(f);
        double root = solver.solve(0, 5, 2.5);  // should return 2
        assertEquals(2.0, root, EPSILON);
    }

    public void testRootAtZero() throws MathException {
        PolynomialFunction f = new PolynomialFunction(new double[]{0.0, 1.0}); // x
        LaguerreSolver solver = new LaguerreSolver(f);
        double root = solver.solve(-1, 1, 0);
        assertEquals(0.0, root, EPSILON);
    }

    public void testNoSignChange() {
        PolynomialFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 1.0}); // x^2 + 1
        LaguerreSolver solver = new LaguerreSolver(f);
        try {
            solver.solve(-5, 5, 0);
            fail("Expected IllegalArgumentException due to no sign change");
        } catch (IllegalArgumentException expected) {
            // ✅ Expected outcome
        } catch (MathException e) {
            fail("Unexpected MathException instead of IllegalArgumentException");
        }
    }

    public void testNullFunction() {
        try {
            new LaguerreSolver(null);
            fail("Expected IllegalArgumentException for null function");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }
}
