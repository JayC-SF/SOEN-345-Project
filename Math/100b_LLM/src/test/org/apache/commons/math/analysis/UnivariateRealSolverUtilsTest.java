package org.apache.commons.math.analysis;

import junit.framework.TestCase;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;

public class UnivariateRealSolverUtilsTest extends TestCase {

    public void testBracketValid() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;  // Roots at x = -2 and x = 2
            }
        };

        // Slight offset from root to avoid exact zero
        double[] result = UnivariateRealSolverUtils.bracket(f, 0.1, -5.0, 5.0);
        double fa = f.value(result[0]);
        double fb = f.value(result[1]);

        assertEquals(2, result.length);
        assertTrue("f(a) and f(b) should have opposite signs or one is a root", fa * fb <= 0);
    }


    public void testBracketValidImproved() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };

        double[] result = UnivariateRealSolverUtils.bracket(f, 3.0, 2.0, 4.0);
        double fa = f.value(result[0]);
        double fb = f.value(result[1]);

        assertEquals(2, result.length);
        assertTrue("f(a) and f(b) should have opposite signs or one is a root", fa * fb <= 0);
    }

    public void testBracketFailure() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 5;  // Always positive in [0, 10]
            }
        };

        try {
            UnivariateRealSolverUtils.bracket(f, 0.0, 1.0, 10.0, 5);
            fail("Expected IllegalArgumentException due to no bracketing interval found");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
