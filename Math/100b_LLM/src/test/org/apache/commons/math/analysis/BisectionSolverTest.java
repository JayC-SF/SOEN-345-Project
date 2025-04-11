/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.math.analysis;

import org.apache.commons.math.MathException;
import org.apache.commons.math.TestUtils;
import junit.framework.TestCase;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.BisectionSolver;
import org.junit.Test;

/**
 * @version $Revision$ $Date$
 */
public final class BisectionSolverTest extends TestCase {

@Test
public void testLinearFunctionRoot() throws Exception {
    UnivariateRealFunction f = x -> x - 5;
    BisectionSolver solver = new BisectionSolver(f);
    double root = solver.solve(0, 10, 0);
    assertEquals(5.0, root, 1e-6);
}

    @Test
    public void testRootAtIntervalEnd() throws Exception {
        UnivariateRealFunction f = x -> x;
        BisectionSolver solver = new BisectionSolver(f);
        double root = solver.solve(0, 1, 0);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testNearZeroRoot() throws Exception {
        UnivariateRealFunction f = x -> x - 1e-8;
        BisectionSolver solver = new BisectionSolver(f);
        double root = solver.solve(0, 1e-7, 0);
        assertEquals(1e-8, root, 1e-6);
    }

    @Test
    public void testNullFunction() {
        try {
            new BisectionSolver(null);
            fail("Expected IllegalArgumentException for null function");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInvalidIntervalOrder() throws Exception {
        try {
            UnivariateRealFunction f = x -> x - 2;
            BisectionSolver solver = new BisectionSolver(f);
            solver.solve(10, 0, 0); // min > max
            fail("Expected IllegalArgumentException for inverted interval");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoSignChange() throws Exception {
        UnivariateRealFunction f = x -> x * x + 1; // No sign change
        BisectionSolver solver = new BisectionSolver(f);
        solver.solve(-2, 2, 0);
    }
   
}
