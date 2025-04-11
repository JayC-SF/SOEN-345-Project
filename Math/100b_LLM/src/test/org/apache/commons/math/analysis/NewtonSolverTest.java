package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import org.apache.commons.math.TestUtils;

import junit.framework.TestCase;

public final class NewtonSolverTest extends TestCase {

    private static final double EPSILON = 1e-6;

    public void testLinearRoot() throws MathException {
        DifferentiableUnivariateRealFunction f = new DifferentiableUnivariateRealFunction() {
            public double value(double x) {
                return x - 3;
            }
            public UnivariateRealFunction derivative() {
                return new UnivariateRealFunction() {
                    public double value(double x) {
                        return 1.0;
                    }
                };
            }
        };

        NewtonSolver solver = new NewtonSolver(f);
        double root = solver.solve(0.0, 10.0, 1.0);
        assertEquals(3.0, root, EPSILON);
    }

    public void testQuadraticRoot() throws MathException {
        DifferentiableUnivariateRealFunction f = new DifferentiableUnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
            public UnivariateRealFunction derivative() {
                return new UnivariateRealFunction() {
                    public double value(double x) {
                        return 2 * x;
                    }
                };
            }
        };

        NewtonSolver solver = new NewtonSolver(f);
        double root = solver.solve(0.0, 5.0, 3.0);
        assertEquals(2.0, root, EPSILON);
    }

    public void testRootAtZero() throws MathException {
        DifferentiableUnivariateRealFunction f = new DifferentiableUnivariateRealFunction() {
            public double value(double x) {
                return x;
            }
            public UnivariateRealFunction derivative() {
                return new UnivariateRealFunction() {
                    public double value(double x) {
                        return 1.0;
                    }
                };
            }
        };

        NewtonSolver solver = new NewtonSolver(f);
        double root = solver.solve(-1.0, 1.0, 0.1);
        assertEquals(0.0, root, EPSILON);
    }

    public void testNullFunction() {
        try {
            new NewtonSolver(null);
            fail("Expected IllegalArgumentException for null function");
        } catch (IllegalArgumentException expected) {
            // Passed
        }
    }

    public void testInvalidInterval() {
        DifferentiableUnivariateRealFunction f = new DifferentiableUnivariateRealFunction() {
            public double value(double x) {
                return x * x + 1;
            }
            public UnivariateRealFunction derivative() {
                return new UnivariateRealFunction() {
                    public double value(double x) {
                        return 2 * x;
                    }
                };
            }
        };

        NewtonSolver solver = new NewtonSolver(f);
        try {
            solver.solve(1.0, -1.0, 0.0); // Invalid interval (min > max)
            fail("Expected IllegalArgumentException for inverted interval");
        } catch (IllegalArgumentException expected) {
            // Passed
        } catch (MathException e) {
            fail("Expected IllegalArgumentException, not MathException");
        }
    }

    public void testNonConvergingFunction() throws MathException {
        DifferentiableUnivariateRealFunction f = new DifferentiableUnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
            public UnivariateRealFunction derivative() {
                return new UnivariateRealFunction() {
                    public double value(double x) {
                        return Math.cos(x);
                    }
                };
            }
        };

        NewtonSolver solver = new NewtonSolver(f);
        double root = solver.solve(3.0, 4.0, 3.1); // Near pi
        assertEquals(Math.PI, root, 1e-6); // Should converge to π
    }

}
