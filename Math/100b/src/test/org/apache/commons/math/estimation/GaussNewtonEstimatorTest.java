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

package org.apache.commons.math.estimation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.math.estimation.EstimatedParameter;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.GaussNewtonEstimator;
import org.apache.commons.math.estimation.WeightedMeasurement;

import junit.framework.*;

/**
 * <p>
 * Some of the unit tests are re-implementations of the MINPACK <a
 * href="http://www.netlib.org/minpack/ex/file17">file17</a> and <a
 * href="http://www.netlib.org/minpack/ex/file22">file22</a> test files.
 * The redistribution policy for MINPACK is available <a
 * href="http://www.netlib.org/minpack/disclaimer">here</a>, for
 * convenience, it is reproduced below.
 * </p>
 * 
 * <table border="0" width="80%" cellpadding="10" align="center" bgcolor=
 * "#E0E0E0">
 * <tr>
 * <td>
 * Minpack Copyright Notice (1999) University of Chicago.
 * All rights reserved
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * <ol>
 * <li>Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution.</li>
 * <li>The end-user documentation included with the redistribution, if any,
 * must include the following acknowledgment:
 * <code>This product includes software developed by the University of
 *           Chicago, as Operator of Argonne National Laboratory.</code>
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.</li>
 * <li><strong>WARRANTY DISCLAIMER. THE SOFTWARE IS SUPPLIED "AS IS"
 * WITHOUT WARRANTY OF ANY KIND. THE COPYRIGHT HOLDER, THE
 * UNITED STATES, THE UNITED STATES DEPARTMENT OF ENERGY, AND
 * THEIR EMPLOYEES: (1) DISCLAIM ANY WARRANTIES, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO ANY IMPLIED WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE
 * OR NON-INFRINGEMENT, (2) DO NOT ASSUME ANY LEGAL LIABILITY
 * OR RESPONSIBILITY FOR THE ACCURACY, COMPLETENESS, OR
 * USEFULNESS OF THE SOFTWARE, (3) DO NOT REPRESENT THAT USE OF
 * THE SOFTWARE WOULD NOT INFRINGE PRIVATELY OWNED RIGHTS, (4)
 * DO NOT WARRANT THAT THE SOFTWARE WILL FUNCTION
 * UNINTERRUPTED, THAT IT IS ERROR-FREE OR THAT ANY ERRORS WILL
 * BE CORRECTED.</strong></li>
 * <li><strong>LIMITATION OF LIABILITY. IN NO EVENT WILL THE COPYRIGHT
 * HOLDER, THE UNITED STATES, THE UNITED STATES DEPARTMENT OF
 * ENERGY, OR THEIR EMPLOYEES: BE LIABLE FOR ANY INDIRECT,
 * INCIDENTAL, CONSEQUENTIAL, SPECIAL OR PUNITIVE DAMAGES OF
 * ANY KIND OR NATURE, INCLUDING BUT NOT LIMITED TO LOSS OF
 * PROFITS OR LOSS OF DATA, FOR ANY REASON WHATSOEVER, WHETHER
 * SUCH LIABILITY IS ASSERTED ON THE BASIS OF CONTRACT, TORT
 * (INCLUDING NEGLIGENCE OR STRICT LIABILITY), OR OTHERWISE,
 * EVEN IF ANY OF SAID PARTIES HAS BEEN WARNED OF THE
 * POSSIBILITY OF SUCH LOSS OR DAMAGES.</strong></li>
 * <ol>
 * </td>
 * </tr>
 * </table>
 * 
 * @author Argonne National Laboratory. MINPACK project. March 1980 (original
 *         fortran minpack tests)
 * @author Burton S. Garbow (original fortran minpack tests)
 * @author Kenneth E. Hillstrom (original fortran minpack tests)
 * @author Jorge J. More (original fortran minpack tests)
 * @author Luc Maisonobe (non-minpack tests and minpack tests Java translation)
 */
public class GaussNewtonEstimatorTest
    extends TestCase {

  public GaussNewtonEstimatorTest(String name) {
    super(name);
  }

  public void testTrivial() throws EstimationException {
    LinearProblem problem = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 2 },
            new EstimatedParameter[] {
                new EstimatedParameter("p0", 0)
            }, 3.0)
    });
    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    estimator.estimate(problem);
    assertEquals(0, estimator.getRMS(problem), 1.0e-10);
    assertEquals(1.5,
        problem.getUnboundParameters()[0].getEstimate(),
        1.0e-10);
  }

  public void testQRColumnsPermutation() throws EstimationException {

    EstimatedParameter[] x = {
        new EstimatedParameter("p0", 0), new EstimatedParameter("p1", 0)
    };
    LinearProblem problem = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 1.0, -1.0 },
            new EstimatedParameter[] { x[0], x[1] },
            4.0),
        new LinearMeasurement(new double[] { 2.0 },
            new EstimatedParameter[] { x[1] },
            6.0),
        new LinearMeasurement(new double[] { 1.0, -2.0 },
            new EstimatedParameter[] { x[0], x[1] },
            1.0)
    });

    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    estimator.estimate(problem);
    assertEquals(0, estimator.getRMS(problem), 1.0e-10);
    assertEquals(7.0, x[0].getEstimate(), 1.0e-10);
    assertEquals(3.0, x[1].getEstimate(), 1.0e-10);

  }

  public void testNoDependency() throws EstimationException {
    EstimatedParameter[] p = new EstimatedParameter[] {
        new EstimatedParameter("p0", 0),
        new EstimatedParameter("p1", 0),
        new EstimatedParameter("p2", 0),
        new EstimatedParameter("p3", 0),
        new EstimatedParameter("p4", 0),
        new EstimatedParameter("p5", 0)
    };
    LinearProblem problem = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 2 }, new EstimatedParameter[] { p[0] }, 0.0),
        new LinearMeasurement(new double[] { 2 }, new EstimatedParameter[] { p[1] }, 1.1),
        new LinearMeasurement(new double[] { 2 }, new EstimatedParameter[] { p[2] }, 2.2),
        new LinearMeasurement(new double[] { 2 }, new EstimatedParameter[] { p[3] }, 3.3),
        new LinearMeasurement(new double[] { 2 }, new EstimatedParameter[] { p[4] }, 4.4),
        new LinearMeasurement(new double[] { 2 }, new EstimatedParameter[] { p[5] }, 5.5)
    });
    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    estimator.estimate(problem);
    assertEquals(0, estimator.getRMS(problem), 1.0e-10);
    for (int i = 0; i < p.length; ++i) {
      assertEquals(0.55 * i, p[i].getEstimate(), 1.0e-10);
    }
  }

  public void testOneSet() throws EstimationException {

    EstimatedParameter[] p = {
        new EstimatedParameter("p0", 0),
        new EstimatedParameter("p1", 0),
        new EstimatedParameter("p2", 0)
    };
    LinearProblem problem = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 1.0 },
            new EstimatedParameter[] { p[0] },
            1.0),
        new LinearMeasurement(new double[] { -1.0, 1.0 },
            new EstimatedParameter[] { p[0], p[1] },
            1.0),
        new LinearMeasurement(new double[] { -1.0, 1.0 },
            new EstimatedParameter[] { p[1], p[2] },
            1.0)
    });

    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    estimator.estimate(problem);
    assertEquals(0, estimator.getRMS(problem), 1.0e-10);
    assertEquals(1.0, p[0].getEstimate(), 1.0e-10);
    assertEquals(2.0, p[1].getEstimate(), 1.0e-10);
    assertEquals(3.0, p[2].getEstimate(), 1.0e-10);

  }

  public void testTwoSets() throws EstimationException {
    EstimatedParameter[] p = {
        new EstimatedParameter("p0", 0),
        new EstimatedParameter("p1", 1),
        new EstimatedParameter("p2", 2),
        new EstimatedParameter("p3", 3),
        new EstimatedParameter("p4", 4),
        new EstimatedParameter("p5", 5)
    };

    double epsilon = 1.0e-7;
    LinearProblem problem = new LinearProblem(new LinearMeasurement[] {

        // 4 elements sub-problem
        new LinearMeasurement(new double[] { 2.0, 1.0, 4.0 },
            new EstimatedParameter[] { p[0], p[1], p[3] },
            2.0),
        new LinearMeasurement(new double[] { -4.0, -2.0, 3.0, -7.0 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            -9.0),
        new LinearMeasurement(new double[] { 4.0, 1.0, -2.0, 8.0 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            2.0),
        new LinearMeasurement(new double[] { -3.0, -12.0, -1.0 },
            new EstimatedParameter[] { p[1], p[2], p[3] },
            2.0),

        // 2 elements sub-problem
        new LinearMeasurement(new double[] { epsilon, 1.0 },
            new EstimatedParameter[] { p[4], p[5] },
            1.0 + epsilon * epsilon),
        new LinearMeasurement(new double[] { 1.0, 1.0 },
            new EstimatedParameter[] { p[4], p[5] },
            2.0)

    });

    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    estimator.estimate(problem);
    assertEquals(0, estimator.getRMS(problem), 1.0e-10);
    assertEquals(3.0, p[0].getEstimate(), 1.0e-10);
    assertEquals(4.0, p[1].getEstimate(), 1.0e-10);
    assertEquals(-1.0, p[2].getEstimate(), 1.0e-10);
    assertEquals(-2.0, p[3].getEstimate(), 1.0e-10);
    assertEquals(1.0 + epsilon, p[4].getEstimate(), 1.0e-10);
    assertEquals(1.0 - epsilon, p[5].getEstimate(), 1.0e-10);

  }

  public void testNonInversible() throws EstimationException {

    EstimatedParameter[] p = {
        new EstimatedParameter("p0", 0),
        new EstimatedParameter("p1", 0),
        new EstimatedParameter("p2", 0)
    };
    LinearMeasurement[] m = new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 1.0, 2.0, -3.0 },
            new EstimatedParameter[] { p[0], p[1], p[2] },
            1.0),
        new LinearMeasurement(new double[] { 2.0, 1.0, 3.0 },
            new EstimatedParameter[] { p[0], p[1], p[2] },
            1.0),
        new LinearMeasurement(new double[] { -3.0, -9.0 },
            new EstimatedParameter[] { p[0], p[2] },
            1.0)
    };
    LinearProblem problem = new LinearProblem(m);

    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    try {
      estimator.estimate(problem);
      fail("an exception should have been caught");
    } catch (EstimationException ee) {
      // expected behavior
    } catch (Exception e) {
      fail("wrong exception type caught");
    }
  }

  public void testIllConditioned() throws EstimationException {
    EstimatedParameter[] p = {
        new EstimatedParameter("p0", 0),
        new EstimatedParameter("p1", 1),
        new EstimatedParameter("p2", 2),
        new EstimatedParameter("p3", 3)
    };

    LinearProblem problem1 = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 10.0, 7.0, 8.0, 7.0 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            32.0),
        new LinearMeasurement(new double[] { 7.0, 5.0, 6.0, 5.0 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            23.0),
        new LinearMeasurement(new double[] { 8.0, 6.0, 10.0, 9.0 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            33.0),
        new LinearMeasurement(new double[] { 7.0, 5.0, 9.0, 10.0 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            31.0)
    });
    GaussNewtonEstimator estimator1 = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    estimator1.estimate(problem1);
    assertEquals(0, estimator1.getRMS(problem1), 1.0e-10);
    assertEquals(1.0, p[0].getEstimate(), 1.0e-10);
    assertEquals(1.0, p[1].getEstimate(), 1.0e-10);
    assertEquals(1.0, p[2].getEstimate(), 1.0e-10);
    assertEquals(1.0, p[3].getEstimate(), 1.0e-10);

    LinearProblem problem2 = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 10.0, 7.0, 8.1, 7.2 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            32.0),
        new LinearMeasurement(new double[] { 7.08, 5.04, 6.0, 5.0 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            23.0),
        new LinearMeasurement(new double[] { 8.0, 5.98, 9.89, 9.0 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            33.0),
        new LinearMeasurement(new double[] { 6.99, 4.99, 9.0, 9.98 },
            new EstimatedParameter[] { p[0], p[1], p[2], p[3] },
            31.0)
    });
    GaussNewtonEstimator estimator2 = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    estimator2.estimate(problem2);
    assertEquals(0, estimator2.getRMS(problem2), 1.0e-10);
    assertEquals(-81.0, p[0].getEstimate(), 1.0e-8);
    assertEquals(137.0, p[1].getEstimate(), 1.0e-8);
    assertEquals(-34.0, p[2].getEstimate(), 1.0e-8);
    assertEquals(22.0, p[3].getEstimate(), 1.0e-8);

  }

  public void testMoreEstimatedParametersSimple() throws EstimationException {

    EstimatedParameter[] p = {
        new EstimatedParameter("p0", 7),
        new EstimatedParameter("p1", 6),
        new EstimatedParameter("p2", 5),
        new EstimatedParameter("p3", 4)
    };
    LinearProblem problem = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 3.0, 2.0 },
            new EstimatedParameter[] { p[0], p[1] },
            7.0),
        new LinearMeasurement(new double[] { 1.0, -1.0, 1.0 },
            new EstimatedParameter[] { p[1], p[2], p[3] },
            3.0),
        new LinearMeasurement(new double[] { 2.0, 1.0 },
            new EstimatedParameter[] { p[0], p[2] },
            5.0)
    });

    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    try {
      estimator.estimate(problem);
      fail("an exception should have been caught");
    } catch (EstimationException ee) {
      // expected behavior
    } catch (Exception e) {
      fail("wrong exception type caught");
    }

  }

  public void testMoreEstimatedParametersUnsorted() throws EstimationException {
    EstimatedParameter[] p = {
        new EstimatedParameter("p0", 2),
        new EstimatedParameter("p1", 2),
        new EstimatedParameter("p2", 2),
        new EstimatedParameter("p3", 2),
        new EstimatedParameter("p4", 2),
        new EstimatedParameter("p5", 2)
    };
    LinearProblem problem = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 1.0, 1.0 },
            new EstimatedParameter[] { p[0], p[1] },
            3.0),
        new LinearMeasurement(new double[] { 1.0, 1.0, 1.0 },
            new EstimatedParameter[] { p[2], p[3], p[4] },
            12.0),
        new LinearMeasurement(new double[] { 1.0, -1.0 },
            new EstimatedParameter[] { p[4], p[5] },
            -1.0),
        new LinearMeasurement(new double[] { 1.0, -1.0, 1.0 },
            new EstimatedParameter[] { p[3], p[2], p[5] },
            7.0),
        new LinearMeasurement(new double[] { 1.0, -1.0 },
            new EstimatedParameter[] { p[4], p[3] },
            1.0)
    });

    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    try {
      estimator.estimate(problem);
      fail("an exception should have been caught");
    } catch (EstimationException ee) {
      // expected behavior
    } catch (Exception e) {
      fail("wrong exception type caught");
    }

  }

  public void testRedundantEquations() throws EstimationException {
    EstimatedParameter[] p = {
        new EstimatedParameter("p0", 1),
        new EstimatedParameter("p1", 1)
    };
    LinearProblem problem = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 1.0, 1.0 },
            new EstimatedParameter[] { p[0], p[1] },
            3.0),
        new LinearMeasurement(new double[] { 1.0, -1.0 },
            new EstimatedParameter[] { p[0], p[1] },
            1.0),
        new LinearMeasurement(new double[] { 1.0, 3.0 },
            new EstimatedParameter[] { p[0], p[1] },
            5.0)
    });

    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    estimator.estimate(problem);
    assertEquals(0, estimator.getRMS(problem), 1.0e-10);
    EstimatedParameter[] all = problem.getAllParameters();
    for (int i = 0; i < all.length; ++i) {
      assertEquals(all[i].getName().equals("p0") ? 2.0 : 1.0,
          all[i].getEstimate(), 1.0e-10);
    }

  }

  public void testInconsistentEquations() throws EstimationException {
    EstimatedParameter[] p = {
        new EstimatedParameter("p0", 1),
        new EstimatedParameter("p1", 1)
    };
    LinearProblem problem = new LinearProblem(new LinearMeasurement[] {
        new LinearMeasurement(new double[] { 1.0, 1.0 },
            new EstimatedParameter[] { p[0], p[1] },
            3.0),
        new LinearMeasurement(new double[] { 1.0, -1.0 },
            new EstimatedParameter[] { p[0], p[1] },
            1.0),
        new LinearMeasurement(new double[] { 1.0, 3.0 },
            new EstimatedParameter[] { p[0], p[1] },
            4.0)
    });

    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    estimator.estimate(problem);
    assertTrue(estimator.getRMS(problem) > 0.1);

  }

  // public void testBoundParameters() throws EstimationException {
  // EstimatedParameter[] p = {
  // new EstimatedParameter("unbound0", 2, false),
  // new EstimatedParameter("unbound1", 2, false),
  // new EstimatedParameter("bound", 2, true)
  // };
  // LinearProblem problem = new LinearProblem(new LinearMeasurement[] {
  // new LinearMeasurement(new double[] { 1.0, 1.0, 1.0 },
  // new EstimatedParameter[] { p[0], p[1], p[2] },
  // 3.0),
  // new LinearMeasurement(new double[] { 1.0, -1.0, 1.0 },
  // new EstimatedParameter[] { p[0], p[1], p[2] },
  // 1.0),
  // new LinearMeasurement(new double[] { 1.0, 3.0, 2.0 },
  // new EstimatedParameter[] { p[0], p[1], p[2] },
  // 7.0)
  // });

  // GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6,
  // 1.0e-6);
  // estimator.estimate(problem);
  // assertTrue(estimator.getRMS(problem) < 1.0e-10);
  // double[][] covariances = estimator.getCovariances(problem);
  // int i0 = 0, i1 = 1;
  // if (problem.getUnboundParameters()[0].getName().endsWith("1")) {
  // i0 = 1;
  // i1 = 0;
  // }
  // assertEquals(11.0 / 24, covariances[i0][i0], 1.0e-10);
  // assertEquals(-3.0 / 24, covariances[i0][i1], 1.0e-10);
  // assertEquals(-3.0 / 24, covariances[i1][i0], 1.0e-10);
  // assertEquals( 3.0 / 24, covariances[i1][i1], 1.0e-10);

  // double[] errors = estimator.guessParametersErrors(problem);
  // assertEquals(0, errors[i0], 1.0e-10);
  // assertEquals(0, errors[i1], 1.0e-10);

  // }

  public void testMaxIterations() {
    Circle circle = new Circle(98.680, 47.345);
    circle.addPoint(30.0, 68.0);
    circle.addPoint(50.0, -6.0);
    circle.addPoint(110.0, -20.0);
    circle.addPoint(35.0, 15.0);
    circle.addPoint(45.0, 97.0);
    try {
      GaussNewtonEstimator estimator = new GaussNewtonEstimator(4, 1.0e-14, 1.0e-14);
      estimator.estimate(circle);
      fail("an exception should have been caught");
    } catch (EstimationException ee) {
      // expected behavior
    } catch (Exception e) {
      fail("wrong exception type caught");
    }
  }

  public void testCircleFitting() throws EstimationException {
    Circle circle = new Circle(98.680, 47.345);
    circle.addPoint(30.0, 68.0);
    circle.addPoint(50.0, -6.0);
    circle.addPoint(110.0, -20.0);
    circle.addPoint(35.0, 15.0);
    circle.addPoint(45.0, 97.0);
    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-10, 1.0e-10);
    estimator.estimate(circle);
    double rms = estimator.getRMS(circle);
    assertEquals(1.768262623567235, Math.sqrt(circle.getM()) * rms, 1.0e-10);
    assertEquals(69.96016176931406, circle.getRadius(), 1.0e-10);
    assertEquals(96.07590211815305, circle.getX(), 1.0e-10);
    assertEquals(48.13516790438953, circle.getY(), 1.0e-10);
  }

  public void testCircleFittingBadInit() throws EstimationException {
    Circle circle = new Circle(-12, -12);
    double[][] points = new double[][] {
        { -0.312967, 0.072366 }, { -0.339248, 0.132965 }, { -0.379780, 0.202724 },
        { -0.390426, 0.260487 }, { -0.361212, 0.328325 }, { -0.346039, 0.392619 },
        { -0.280579, 0.444306 }, { -0.216035, 0.470009 }, { -0.149127, 0.493832 },
        { -0.075133, 0.483271 }, { -0.007759, 0.452680 }, { 0.060071, 0.410235 },
        { 0.103037, 0.341076 }, { 0.118438, 0.273884 }, { 0.131293, 0.192201 },
        { 0.115869, 0.129797 }, { 0.072223, 0.058396 }, { 0.022884, 0.000718 },
        { -0.053355, -0.020405 }, { -0.123584, -0.032451 }, { -0.216248, -0.032862 },
        { -0.278592, -0.005008 }, { -0.337655, 0.056658 }, { -0.385899, 0.112526 },
        { -0.405517, 0.186957 }, { -0.415374, 0.262071 }, { -0.387482, 0.343398 },
        { -0.347322, 0.397943 }, { -0.287623, 0.458425 }, { -0.223502, 0.475513 },
        { -0.135352, 0.478186 }, { -0.061221, 0.483371 }, { 0.003711, 0.422737 },
        { 0.065054, 0.375830 }, { 0.108108, 0.297099 }, { 0.123882, 0.222850 },
        { 0.117729, 0.134382 }, { 0.085195, 0.056820 }, { 0.029800, -0.019138 },
        { -0.027520, -0.072374 }, { -0.102268, -0.091555 }, { -0.200299, -0.106578 },
        { -0.292731, -0.091473 }, { -0.356288, -0.051108 }, { -0.420561, 0.014926 },
        { -0.471036, 0.074716 }, { -0.488638, 0.182508 }, { -0.485990, 0.254068 },
        { -0.463943, 0.338438 }, { -0.406453, 0.404704 }, { -0.334287, 0.466119 },
        { -0.254244, 0.503188 }, { -0.161548, 0.495769 }, { -0.075733, 0.495560 },
        { 0.001375, 0.434937 }, { 0.082787, 0.385806 }, { 0.115490, 0.323807 },
        { 0.141089, 0.223450 }, { 0.138693, 0.131703 }, { 0.126415, 0.049174 },
        { 0.066518, -0.010217 }, { -0.005184, -0.070647 }, { -0.080985, -0.103635 },
        { -0.177377, -0.116887 }, { -0.260628, -0.100258 }, { -0.335756, -0.056251 },
        { -0.405195, -0.000895 }, { -0.444937, 0.085456 }, { -0.484357, 0.175597 },
        { -0.472453, 0.248681 }, { -0.438580, 0.347463 }, { -0.402304, 0.422428 },
        { -0.326777, 0.479438 }, { -0.247797, 0.505581 }, { -0.152676, 0.519380 },
        { -0.071754, 0.516264 }, { 0.015942, 0.472802 }, { 0.076608, 0.419077 },
        { 0.127673, 0.330264 }, { 0.159951, 0.262150 }, { 0.153530, 0.172681 },
        { 0.140653, 0.089229 }, { 0.078666, 0.024981 }, { 0.023807, -0.037022 },
        { -0.048837, -0.077056 }, { -0.127729, -0.075338 }, { -0.221271, -0.067526 }
    };
    for (int i = 0; i < points.length; ++i) {
      circle.addPoint(points[i][0], points[i][1]);
    }
    GaussNewtonEstimator estimator = new GaussNewtonEstimator(100, 1.0e-6, 1.0e-6);
    try {
      estimator.estimate(circle);
      fail("an exception should have been caught");
    } catch (EstimationException ee) {
      // expected behavior
    } catch (Exception e) {
      fail("wrong exception type caught");
    }
  }

  private static class LinearProblem extends SimpleEstimationProblem {

    public LinearProblem(LinearMeasurement[] measurements) {
      HashSet set = new HashSet();
      for (int i = 0; i < measurements.length; ++i) {
        addMeasurement(measurements[i]);
        EstimatedParameter[] parameters = measurements[i].getParameters();
        for (int j = 0; j < parameters.length; ++j) {
          set.add(parameters[j]);
        }
      }
      for (Iterator iterator = set.iterator(); iterator.hasNext();) {
        addParameter((EstimatedParameter) iterator.next());
      }
    }

  }

  private static class LinearMeasurement extends WeightedMeasurement {

    public LinearMeasurement(double[] factors, EstimatedParameter[] parameters,
        double setPoint) {
      super(1.0, setPoint, true);
      this.factors = factors;
      this.parameters = parameters;
      setIgnored(false);
    }

    public double getTheoreticalValue() {
      double v = 0;
      for (int i = 0; i < factors.length; ++i) {
        v += factors[i] * parameters[i].getEstimate();
      }
      return v;
    }

    public double getPartial(EstimatedParameter parameter) {
      for (int i = 0; i < parameters.length; ++i) {
        if (parameters[i] == parameter) {
          return factors[i];
        }
      }
      return 0;
    }

    public EstimatedParameter[] getParameters() {
      return parameters;
    }

    private double[] factors;
    private EstimatedParameter[] parameters;
    private static final long serialVersionUID = -3922448707008868580L;

  }

  private static class Circle implements EstimationProblem {

    public Circle(double cx, double cy) {
      this.cx = new EstimatedParameter("cx", cx);
      this.cy = new EstimatedParameter(new EstimatedParameter("cy", cy));
      points = new ArrayList();
    }

    public void addPoint(double px, double py) {
      points.add(new PointModel(px, py));
    }

    public int getM() {
      return points.size();
    }

    public WeightedMeasurement[] getMeasurements() {
      return (WeightedMeasurement[]) points.toArray(new PointModel[points.size()]);
    }

    public EstimatedParameter[] getAllParameters() {
      return new EstimatedParameter[] { cx, cy };
    }

    public EstimatedParameter[] getUnboundParameters() {
      return new EstimatedParameter[] { cx, cy };
    }

    public double getPartialRadiusX() {
      double dRdX = 0;
      for (Iterator iterator = points.iterator(); iterator.hasNext();) {
        dRdX += ((PointModel) iterator.next()).getPartialDiX();
      }
      return dRdX / points.size();
    }

    public double getPartialRadiusY() {
      double dRdY = 0;
      for (Iterator iterator = points.iterator(); iterator.hasNext();) {
        dRdY += ((PointModel) iterator.next()).getPartialDiY();
      }
      return dRdY / points.size();
    }

    public double getRadius() {
      double r = 0;
      for (Iterator iterator = points.iterator(); iterator.hasNext();) {
        r += ((PointModel) iterator.next()).getCenterDistance();
      }
      return r / points.size();
    }

    public double getX() {
      return cx.getEstimate();
    }

    public double getY() {
      return cy.getEstimate();
    }

    private class PointModel extends WeightedMeasurement {

      public PointModel(double px, double py) {
        super(1.0, 0.0);
        this.px = px;
        this.py = py;
      }

      public double getPartial(EstimatedParameter parameter) {
        if (parameter == cx) {
          return getPartialDiX() - getPartialRadiusX();
        } else if (parameter == cy) {
          return getPartialDiY() - getPartialRadiusY();
        }
        return 0;
      }

      public double getCenterDistance() {
        double dx = px - cx.getEstimate();
        double dy = py - cy.getEstimate();
        return Math.sqrt(dx * dx + dy * dy);
      }

      public double getPartialDiX() {
        return (cx.getEstimate() - px) / getCenterDistance();
      }

      public double getPartialDiY() {
        return (cy.getEstimate() - py) / getCenterDistance();
      }

      public double getTheoreticalValue() {
        return getCenterDistance() - getRadius();
      }

      private double px;
      private double py;
      private static final long serialVersionUID = 1L;

    }

    private EstimatedParameter cx;
    private EstimatedParameter cy;
    private ArrayList points;

  }

  public static Test suite() {
    return new TestSuite(GaussNewtonEstimatorTest.class);
  }

}
