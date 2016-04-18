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

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.response.NullResponse;
import org.apache.wicket.response.StringResponse;

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

    /**
     * http://grepcode.com/file/repo1.maven.org/maven2/org.apache.wicket/wicket-core/7.0.0/org/apache/wicket/markup/head/internal/HeaderResponse.java#HeaderResponse.getRealResponse%28%29
     */
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
    private boolean closed;

    @Override
    public void markRendered(Object object) {
        rendered.add(object);
    }

    @Override
    public void render(HeaderItem item) {
        if (!closed && !wasItemRendered(item)) {
            item.render(getResponse());
            markItemRendered(item);
        }
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
        closed = true;
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
        return RequestCycle.get().getResponse();
    }
}
