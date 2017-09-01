package com.sap.webservice;

import java.io.File;
import com.sap.hybris.*;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author I328800
 */
public class HandleGETRequests implements HttpHandler {

	@Override
	public void handle(HttpExchange he) throws IOException {
		String directoryName = "C:\\mock\\hybris";
		ArrayList<File> files = new ArrayList<File>();
		HashSet<String> itemTypeHashSet = new HashSet<String>();
		GenerateCode.listf(directoryName, files);
		GenerateCode.extractItemType(files, itemTypeHashSet);
		String response = printHashSet(itemTypeHashSet);

		he.sendResponseHeaders(200, response.length());
		OutputStream os = he.getResponseBody();
		os.write(response.toString().getBytes());
		os.close();
	}

	public static String printHashSet(HashSet<String> itemTypeHashSet) {
		Iterator<String> itr = itemTypeHashSet.iterator();
		String res = "{\"itemtypes\":";
		List itemtypes = new ArrayList<String>();
		while (itr.hasNext()) {
			itemtypes.add("\""+itr.next()+"\"");
		}
		res += itemtypes + "}";
		return res;
	}
}
