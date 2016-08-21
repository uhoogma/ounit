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
package com.googlecode.ounit.interceptors;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.message.Message;

/**
 * @author Urmas Hoogma
 * @see
 * <a href="http://stackoverflow.com/a/11053289/">http://stackoverflow.com/a/11053289/</a>
 */
public class OutgoingInterceptor extends AbstractSoapInterceptor {

    public OutgoingInterceptor() {
        super(Phase.PRE_STREAM);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        message.put(Message.ENCODING, "utf-8");
    }
}
