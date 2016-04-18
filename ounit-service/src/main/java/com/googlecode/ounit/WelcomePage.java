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
package com.googlecode.ounit;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

public class WelcomePage extends BasePage {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private static final long serialVersionUID = 1L;

    public WelcomePage(PageParameters parameters) {
        super(parameters);
        add(new HeaderResponseContainer("someId", "footer-container"));
        System.out.println(" add(new HeaderResponseContainer(\"someId\", \"footer-container\"));");
        //  String kala = "";
        log.debug("WelcomePage()");
        HiddenField<String> revision = new HiddenField<String>("revision");
        mainForm.add(revision);
        mainForm.add(new Button("start") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                OunitSession sess = getOunitSession();
                sess.prepare();
                setResponsePage(MainPage.class);
            }
        });

        if (getOunitSession().isClosed()) {
            setResponsePage(MainPage.class);
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        //  super.renderHead(response);
        // ContextRelativeResourceReference crf = new ContextRelativeResourceReference("jquery/jquery.min.js");
        // response.render(JavaScriptHeaderItem.forReference(crf));
        /*
        ContextRelativeResourceReference bootstrapJavascript = new ContextRelativeResourceReference("jquery/jquery.min.js") {
            // use wicket's jquery lib for Bootstrap
            @Override
            public List<HeaderItem> getDependencies() {
                Application application = Application.get();
                ResourceReference jqueryRef = application.getJavaScriptLibrarySettings().
                        getJQueryReference();

                return Arrays.asList(new HeaderItem[]{JavaScriptHeaderItem.forReference(jqueryRef)});
            }
        };
        response.render(JavaScriptHeaderItem.forReference(bootstrapJavascript));
         */

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(MainPage.class,
                "jquery/jquery.min.js")));
        //  response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(MainPage.class, "jquery/jquery.min.js")));
        //  response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(MainPage.class, "jquery/jquery.min.js")));
        response.render(JavaScriptHeaderItem.forScript("jQuery.noConflict();", "jQnoConflict"));
        response.render(CssHeaderItem.forReference(new CssResourceReference(MainPage.class, "ounit.css")));
    }
}
