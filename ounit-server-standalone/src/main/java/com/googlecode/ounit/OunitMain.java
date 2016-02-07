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

import java.util.logging.LogManager;

import javax.xml.ws.Endpoint;

import com.googlecode.ounit.OunitService;

public class OunitMain {
	public static void main(String [] args) {
		
		try {
			// System.setProperty("java.util.logging.config.file", "/home/anttix/tmp/logging.properties");
			LogManager.getLogManager().readConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OunitMain.class);
		log.info("Starting Server");
		OunitService implementor = new OunitService();
		String address = "http://localhost:8080/ounit-server/OunitService";
		Endpoint.publish(address, implementor);
	}
}
