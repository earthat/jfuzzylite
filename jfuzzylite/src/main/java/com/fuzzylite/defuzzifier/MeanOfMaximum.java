/*
 Copyright (C) 2010-2016 by FuzzyLite Limited.
 All rights reserved.

 This file is part of jfuzzylite(TM).

 jfuzzylite is free software: you can redistribute it and/or modify it under
 the terms of the FuzzyLite License included with the software.

 You should have received a copy of the FuzzyLite License along with
 jfuzzylite. If not, see <http://www.fuzzylite.com/license/>.

 fuzzylite(R) is a registered trademark of FuzzyLite Limited.
 jfuzzylite(TM) is a trademark of FuzzyLite Limited.

 */
package com.fuzzylite.defuzzifier;

import com.fuzzylite.Op;
import com.fuzzylite.term.Term;

public class MeanOfMaximum extends IntegralDefuzzifier {

    public MeanOfMaximum() {
        super();
    }

    public MeanOfMaximum(int resolution) {
        super(resolution);
    }

    @Override
    public double defuzzify(Term term, double minimum, double maximum) {
        if (!Op.isFinite(minimum + maximum)) {
            return Double.NaN;
        }

        final int resolution = getResolution();
        final double dx = (maximum - minimum) / resolution;
        double x, y;
        double ymax = -1.0;
        double xsmallest = minimum;
        double xlargest = maximum;
        boolean samePlateau = false;
        for (int i = 0; i < resolution; ++i) {
            x = minimum + (i + 0.5) * dx;
            y = term.membership(x);

            if (Op.isGt(y, ymax)) {
                ymax = y;

                xsmallest = x;
                xlargest = x;

                samePlateau = true;
            } else if (samePlateau && Op.isEq(y, ymax)) {
                xlargest = x;
            } else if (Op.isLt(y, ymax)) {
                samePlateau = false;
            }
        }

        return (xlargest + xsmallest) / 2.0;
    }

    @Override
    public MeanOfMaximum clone() throws CloneNotSupportedException {
        return (MeanOfMaximum) super.clone();
    }

}
