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
package org.apache.wicket.extensions.protocol.opaque;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.internal.HeaderResponse;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.response.NullResponse;
import org.apache.wicket.response.StringResponse;
import org.apache.wicket.response.filter.IResponseFilter;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

/*
 * In the "normal" world we would just capture all headers with DecoratingHeaderResponse
 * and be done with it. But since OPAQUE implementation in Moodle has it's quirks
 * we have to do some ugly hacks.
 *
 * The reason for this hackery is that although header contributions are possible,
 * they are not filtered for %%RESOURCES%% thus we can not send links directly to
 * the engine. We have to pump JavaScript directly to the page body, send external
 * css references straight to the header and convert CSS that points to resources
 * into some ugly JavaScript. To add insult to injury, OPAQUE replaces %%RESOURCES%%
 * with a query string that has &-s encoded to &amp;-s so this needs to be decoded first.
 * Go figure!
 *
 * TODO: Remove this code and replace it with the commented-out block below when
 *       qtype_opaque is fixed.
 * FIXME: Must be able to handle JavaScript resources that contain %%IDPREFIX%%-s
 */
public class OpaqueHeaderResponse implements IHeaderResponse {

    // http://grepcode.com/file/repo1.maven.org/maven2/org.apache.wicket/wicket-core/7.0.0/org/apache/wicket/markup/head/internal/HeaderResponse.java#HeaderResponse.getRealResponse%28%29
    Response real;
    StringResponse buf = new StringResponse();
    Response active = buf;
    OpaqueResponse opaqueResponse;

    OpaqueHeaderResponse(IHeaderResponse response) {
        real = response.getResponse();

        RequestCycle requestCycle = RequestCycle.get();
        if (!(requestCycle.getOriginalResponse() instanceof OpaqueResponse)) {
            throw new IllegalArgumentException(
                    "Opaque header extractor can only be used with OpaqueResponse");
        }

        opaqueResponse = (OpaqueResponse) requestCycle.getOriginalResponse();
    }

    private final Set<Object> rendered = new HashSet<>();
    private final Set<String> processed = new HashSet<>();
    private boolean closed;
    private int prevLength = 0;
    //  private boolean first = true;

    @Override
    public void markRendered(Object object) {
        rendered.add(object);
    }

    @Override
    public void render(HeaderItem item) {
        if (!closed && !wasItemRendered(item)) {
            /*
            String resp = getResponse().toString();
            System.out.println("itemasstring " + item.getRenderTokens().toString());
            if (resp.contains("src=\"./") || resp.contains("href=\"./")) {
                String res = resp.replace("src=\"./", "  src=\"").replace("href=\"./", "  href=\"").trim();
                String frg = res.substring(prevLength, res.length());
                System.out.println("right branch prevLength \n" + prevLength + "\nres.length() " + res.length() + "\nfrg\n" + frg + "\nfrglopp");
                prevLength = res.length() - frg.length();
                getResponse().write(frg);
                processed.add(frg);
                System.out.println("processed size:" + processed.size() + " " + debug());
                item.render(getResponse());
                markItemRendered(item);
                // first = false;
            } else {
                System.out.println("other branch " + resp);
                item.render(getResponse());
                markItemRendered(item);
            }*/
            if (item instanceof org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem) {
                // javascript-./opaque-resources/jquery.min-ver-E85AED5C30D734F1E30646E030D7A817.js
                // class java.lang.String
                // item = foo(item);
                System.out.println("item.toString()" + foo(item));

            }
            item.render(getResponse());
            markItemRendered(item);
            //  rendered.add(item);
        }
    }

    // debug
    public HeaderItem foo(HeaderItem item) {

        StringBuilder sb = new StringBuilder();
        for (Object s : item.getRenderTokens()) {
            sb.append("\ntokns start\n");
            String str = s.toString();
            String end = null;
            if (str.startsWith("javascript-./")) {
                end = str.replace("./", "");
            }
            s = end;
            sb.append(s.toString() + " " + s.getClass().toString());
            sb.append("\ntokensend\n");
        }
        return item;
    }

    protected boolean wasItemRendered(HeaderItem item) {
        for (Object curToken : item.getRenderTokens()) {
            if (wasRendered(curToken)) {
                return true;
            }
        }
        return false;
    }

    protected void markItemRendered(HeaderItem item) {

        for (Object curToken : item.getRenderTokens()) {
            markRendered(curToken);
        }
    }

    @Override
    public boolean wasRendered(Object object) {
        return rendered.contains(object);
    }

    @Override
    public void close() {
        /*AppendingStringBuffer a = new AppendingStringBuffer(buf.getBuffer());
        for (IResponseFilter f : Application.get().getRequestCycleSettings()
                .getResponseFilters()) {
            f.filter(a);
        }
        opaqueResponse.addHeaderContribution(a.toString());
        buf.reset();*/
        closed = true;
    }

    // debug
    public String debug() {

        StringBuilder sb = new StringBuilder();
        for (Object s : processed.toArray()) {
            sb.append("\nitem start\n");
            sb.append(s.toString());
            sb.append("\nitem end\n");
        }
        return sb.toString();
    }

    @Override
    public Response getResponse() {
        return closed ? NullResponse.getInstance() : getRealResponse();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    protected Response getRealResponse() {
        return real; //RequestCycle.get().getResponse();
    }

    /*
    @Override
    public void renderCSSReference(ResourceReference reference,
            PageParameters pageParameters, String media, String condition) {
        if (reference == null) {
            throw new IllegalArgumentException("reference cannot be null");
        }
        if (!isClosed()) {
            IRequestHandler handler = new ResourceReferenceRequestHandler(
                    reference, pageParameters);
            CharSequence url = RequestCycle.get().urlFor(handler);
            renderCSSReference(url.toString(), media, condition);
        }
    }

    @Override
    public void renderCSSReference(String url, String media, String condition) {
        if (Strings.isEmpty(condition) == false) {
            real.write("<!--[if ");
            real.write(condition);
            real.write("]>");
        }

        List<String> token = Arrays.asList("css", url.toString(), media);
        real.write("<script type=\"text/javascript\">");
        real.write("var elem=document.createElement(\"link\");");
        real.write("elem.setAttribute(\"rel\",\"stylesheet\");");
        real.write("elem.setAttribute(\"type\",\"text/css\");");
        real.write("elem.setAttribute(\"href\", \"" + url + "\".replace(/&amp;/g, '&'));");
        if (media != null) {
            real.write("elem.setAttribute(\"media\",\"" + media + "\");");
        }
        real.write("document.getElementsByTagName(\"head\")[0].appendChild(elem);");
        real.write("</script>");
        real.write("\n");
        markRendered(token);

        if (Strings.isEmpty(condition) == false) {
            real.write("<![endif]-->");
        }
    }

    @Override
    public void renderCSS(CharSequence css, String id) {
        active = real;
        super.renderCSS(css, id);
        active = buf;
    }

    @Override
    public void renderJavaScript(CharSequence javascript, String id) {
        active = real;
        super.renderJavaScript(javascript, id);
        active = buf;
    }

    @Override
    public void renderJavaScriptReference(ResourceReference reference,
            PageParameters pageParameters, String id) {
        active = real;
        super.renderJavaScriptReference(reference, pageParameters, id);
        active = buf;
    }

    @Override
    public void renderJavaScriptReference(java.lang.String url,
            java.lang.String id, boolean defer) {
        active = real;
        super.renderJavaScriptReference(url, id, defer);
        active = buf;
    }

    @Override
    public void renderOnEventJavaScript(String target, String event,
            String javascript) {
        active = real;
        super.renderOnEventJavaScript(target, event, javascript);
        active = buf;
    }

    @Override
    public void close() {
        super.close();
        AppendingStringBuffer a = new AppendingStringBuffer(buf.getBuffer());
        for (IResponseFilter f : Application.get().getRequestCycleSettings()
                .getResponseFilters()) {
            f.filter(a);
        }
        opaqueResponse.addHeaderContribution(a.toString());
        buf.reset();
    }
     */
 /*
    public void onRendered(final Component component) {
        super.onRendered(component);
        HeaderResponse headerResponse = new HeaderResponse() {

            @Override
            protected Response getRealResponse() {
                return component.getResponse();
            }
        };
        headerResponse.renderOnDomReadyJavascript(getNiftyJS(tagName));
    }
     */
 /*
    @Override
    protected Response getRealResponse() {
        return super.getResponse();
    }

     */
 /*
app.setHeaderResponseDecorator(new IHeaderResponseDecorator() {
	public IHeaderResponse decorate(final IHeaderResponse response) {
		return new DecoratingHeaderResponse(response) {
			StringResponse buf = new StringResponse();

			@Override
			public void close() {
				AppendingStringBuffer a = new AppendingStringBuffer(buf.getBuffer());
				for(IResponseFilter f: app.getRequestCycleSettings().getResponseFilters()) {
					f.filter(a);
				}
				OpaqueReturn.get().setHeader(a.toString());
				super.close();
				buf.reset();
			}
			@Override
			public Response getResponse() {
				return buf;
			}
		};
	}
});
     */
}
