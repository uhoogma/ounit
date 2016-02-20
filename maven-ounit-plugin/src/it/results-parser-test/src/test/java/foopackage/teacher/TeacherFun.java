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
 */
package foopackage.teacher;

import org.junit.*;
import static org.junit.Assert.*;

public class TeacherFun {

    // @Test
    public void testThatAtLeastOneThingIsOK() {
        assertTrue(true);
    }

    //@Test
    public void testIfYouDidActuallySolveIt() {
        assertTrue(false);
    }

    //@Test
    public void testIfAnybodyLikesYouAfterAll() {
        assertEquals("Who likes you punk?", "everybody", "nobody");
    }

    @Test
    public void test() {
        assertEquals("Who likes you punk?", "everybody", "everybody");
    }
}
