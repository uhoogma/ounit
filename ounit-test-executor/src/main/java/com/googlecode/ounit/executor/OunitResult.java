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
package com.googlecode.ounit.executor;

import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.plugin.AbstractMojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class OunitResult {

    @SuppressWarnings("FieldMayBeFinal")
    private MavenExecutionResult r;

    public OunitResult(MavenExecutionResult r) {
        this.r = r;
    }

    public boolean hasErrors() {
        return r.hasExceptions();
    }

    public boolean hasCompileErrors() {
        if (r.hasExceptions()) {
            if (r.getExceptions()
                    .stream()
                    .filter((t) -> (t.getCause() != null))
                    .map((t) -> t.getCause())
                    .map((t) -> t.getClass()
                            .getCanonicalName())
                    .anyMatch((s) -> (s.contains("CompilationFailureException")))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasTestErrors() {
        if (r.hasExceptions()) {
            if (r.getExceptions().stream().map((t) -> {
                if (t.getCause() != null) {
                    t = t.getCause();
                }
                return t;
            }).filter((t) -> (t instanceof MojoFailureException))
                    .map((t) -> t.getMessage())
                    .anyMatch((s) -> (s != null && s.contains("test failures")))) {
                return true;
            }
        }
        return false;
    }

    public String getErrors() {
        if (!hasErrors()) {
            return null;
        }
        String rv = "";

        for (Throwable t : r.getExceptions()) {
            if (t.getCause() != null) {
                t = t.getCause();
            }

            rv += t.getClass().getCanonicalName() + ": " + t.getMessage() + "\n";

            if (t instanceof AbstractMojoExecutionException) {
                String s = ((AbstractMojoExecutionException) t).getLongMessage();
                if (s != null) {
                    rv += s + "\n";
                }
            }
        }
        return rv;
    }

    public String getOutputDirectory() {
        try {
            return r.getProject().getBuild().getDirectory();
        } catch (NullPointerException e) {
            return null;
        }
    }
}
