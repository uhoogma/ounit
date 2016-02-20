/*
 * OUnit - an OPAQUE compliant framework for Computer Aided Testing
 *
 * Copyright (C) 2010, 2011  Antti Andreimann
 *
 * This file is part of OUnit.
 *
 * OUnit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OUnit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OUnit.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contains pieces of code from OpenMark online assessment system
 * SVN r437 (2011-04-21)
 *
 * Copyright (C) 2007 The Open University
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package com.googlecode.ounit.opaque;

/*
 * API CLASS: This class is used in SOAP returns and should not be altered
 */
/**
 * Single score
 */
public class Score {

    public Score() {
    }

    /**
     * Create a score
     *
     * @param axis preliminary description
     * @param marks preliminary description
     */
    public Score(String axis, int marks) {
        this.axis = axis;
        this.marks = marks;
    }

    private int marks;
    private String axis;

    /**
     * @return Score axis (null for default)
     */
    public String getAxis() {
        return axis;
    }

    /**
     * @return Number of marks achieved for question (or, maximum for this axis,
     * in that context)
     */
    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }
}
