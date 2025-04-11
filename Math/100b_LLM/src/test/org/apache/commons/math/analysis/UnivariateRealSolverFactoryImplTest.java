package org.apache.commons.math.analysis;

import junit.framework.TestCase;

public class UnivariateRealSolverFactoryImplTest extends TestCase {

    private static final UnivariateRealFunction testFunction = new UnivariateRealFunction() {
        public double value(double x) {
            return x * x - 4; // Root at x = ±2
        }
    };

    public void testDefaultSolver() {
        UnivariateRealSolverFactoryImpl factory = new UnivariateRealSolverFactoryImpl();
        UnivariateRealSolver solver = factory.newDefaultSolver(testFunction);
        double result = 0.0;
        try {
            result = solver.solve(1.0, 3.0); // Root between 1 and 3 is 2
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(2.0, result, 1.0e-6);
    }

    public void testBisectionSolver() {
        UnivariateRealSolverFactoryImpl factory = new UnivariateRealSolverFactoryImpl();
        UnivariateRealSolver solver = factory.newBisectionSolver(testFunction);
        double result = 0.0;
        try {
            result = solver.solve(0.0, 5.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(2.0, result, 1.0e-6);
    }

    public void testBrentSolver() {
        UnivariateRealSolverFactoryImpl factory = new UnivariateRealSolverFactoryImpl();
        UnivariateRealSolver solver = factory.newBrentSolver(testFunction);
        double result = 0.0;
        try {
            result = solver.solve(1.0, 3.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(2.0, result, 1.0e-6);
    }

    public void testSecantSolver() {
        UnivariateRealSolverFactoryImpl factory = new UnivariateRealSolverFactoryImpl();
        UnivariateRealSolver solver = factory.newSecantSolver(testFunction);
        double result = 0.0;
        try {
            result = solver.solve(1.0, 3.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(2.0, result, 1.0e-6);
    }

    public void testAllSolversGiveConsistentResults() {
        UnivariateRealSolverFactoryImpl factory = new UnivariateRealSolverFactoryImpl();
        try {
            double defaultRoot = factory.newDefaultSolver(testFunction).solve(1.0, 3.0);
            double bisectionRoot = factory.newBisectionSolver(testFunction).solve(1.0, 3.0);
            double brentRoot = factory.newBrentSolver(testFunction).solve(1.0, 3.0);
            double secantRoot = factory.newSecantSolver(testFunction).solve(1.0, 3.0);

            assertEquals(defaultRoot, bisectionRoot, 1.0e-6);
            assertEquals(brentRoot, secantRoot, 1.0e-6);
            assertEquals(2.0, defaultRoot, 1.0e-6);
        } catch (Exception e) {
            fail("Unexpected exception during consistency test: " + e.getMessage());
        }
    }
}
