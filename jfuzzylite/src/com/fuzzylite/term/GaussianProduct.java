/*   Copyright 2013 Juan Rada-Vilela

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package com.fuzzylite.term;

import com.fuzzylite.Op;
import static com.fuzzylite.Op.str;

/**
 *
 * @author jcrada
 */
public class GaussianProduct extends Term {

    protected double meanA, standardDeviationA;
    protected double meanB, standardDeviationB;

    public GaussianProduct(String name) {
        this(name, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
    }

    public GaussianProduct(String name, double meanA, double standardDeviationA,
            double meanB, double standardDeviationB) {
        this.name = name;
        this.meanA = meanA;
        this.standardDeviationA = standardDeviationA;
        this.meanB = meanB;
        this.standardDeviationB = standardDeviationB;
    }

    @Override
    public double membership(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        int xLEa = Op.isLE(x, meanA) ? 1 : 0;
        double a = Math.exp((-(x - meanA) * (x - meanA)) / (2 * standardDeviationA * standardDeviationA))
                * xLEa + (1 - xLEa);
        int xGEb = Op.isGE(x, meanB) ? 1 : 0;
        double b = Math.exp((-(x - meanB) * (x - meanB)) / (2 * standardDeviationB * standardDeviationB))
                * xGEb + (1 - xGEb);
        return a * b;
    }

    @Override
    public String toString() {
        String result = GaussianProduct.class.getSimpleName();
        result += "(" + Op.join(", ", str(meanA), str(standardDeviationA),
                str(meanB), str(standardDeviationB)) + ")";
        return result;
    }

    public double getMeanA() {
        return meanA;
    }

    public void setMeanA(double meanA) {
        this.meanA = meanA;
    }

    public double getStandardDeviationA() {
        return standardDeviationA;
    }

    public void setStandardDeviationA(double standardDeviationA) {
        this.standardDeviationA = standardDeviationA;
    }

    public double getMeanB() {
        return meanB;
    }

    public void setMeanB(double meanB) {
        this.meanB = meanB;
    }

    public double getStandardDeviationB() {
        return standardDeviationB;
    }

    public void setStandardDeviationB(double standardDeviationB) {
        this.standardDeviationB = standardDeviationB;
    }

    @Override
    public void configure(double[] parameters) {
        int required = 4;
        if (parameters.length < required) {
            throw new RuntimeException(String.format(
                    "[configuration error] term <%s> requires <%d> parameters",
                    this.getClass().getSimpleName(), required));
        }
        setMeanA(parameters[0]);
        setStandardDeviationA(parameters[1]);
        setMeanB(parameters[2]);
        setStandardDeviationB(parameters[3]);
    }
}
