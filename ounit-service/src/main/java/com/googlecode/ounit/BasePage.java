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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.Application;
import org.apache.wicket.extensions.protocol.opaque.OpaquePage;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.mount.MountMapper;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContextRelativeResourceReference;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Base page for all Ounit engine pages. This class will make sure generic CSS
 * files are loaded, main form properly set up, SessionBean populated etc.
 *
 * @author anttix
 *
 */
public class BasePage extends OpaquePage {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("Convert2Diamond")
    public BasePage(PageParameters parameters) {
        super(parameters);
        //  add(new HeaderResponseContainer("footer-container", "footer-container"));
        // System.out.println(" add(new HeaderResponseContainer(\"footer-container\", \"footer-container\"));");
        /* We use OunitSession as a read-only bean for all page components */
        setDefaultModel(new CompoundPropertyModel<OunitSession>(
                new AbstractReadOnlyModel<OunitSession>() {
            private static final long serialVersionUID = 1L;

            @Override
            public OunitSession getObject() {
                return OunitSession.get();
            }
        }));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
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

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(MainPage.class, "jquery/jquery.min.js")));
        //   response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(MainPage.class, "jquery/jquery.min.js")));
        //  response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(MainPage.class, "jquery/jquery.min.js")));
        response.render(JavaScriptHeaderItem.forScript("jQuery.noConflict();", "jQnoConflict"));
        response.render(CssHeaderItem.forReference(new CssResourceReference(MainPage.class, "ounit.css")));
    }

    @SuppressWarnings("unchecked")
    public IModel<OunitSession> getOunitModel() {
        return (IModel<OunitSession>) getDefaultModel();
    }

    public OunitSession getOunitSession() {
        return getOunitModel().getObject();
    }
}
