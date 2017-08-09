package com.sap.automation;

import java.io.IOException;
/**
*
* @author I328800
*/
import java.net.InetSocketAddress;

import com.sap.codeautomation.AddAttributeToExistingType;
import com.sap.codeautomation.NewItemType;
import com.sap.jsonmodels.InputData;
import com.sap.webservice.HandleGETRequests;
import com.sun.net.httpserver.HttpServer;

public class CodeGeneration {

	public static void generateFiles(InputData requestParameters) {
		//StartWebService();
		boolean addAttribute = requestParameters.isAddAttribute();
		if (addAttribute) {
			AddAttributeToExistingType.AddAttributes(requestParameters);
		} else {
			NewItemType.CreateNewItemType(requestParameters);
		}
	}

	/*public static void StartWebService() {
		HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress(8080), 0);
			System.out.println("server started at " + 8080);
			server.createContext("/codeGenerator", new HandleGETRequests());
			server.setExecutor(null);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting
		// code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.
		 * html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(CodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(CodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(CodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(CodeGeneration.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		// </editor-fold>
	}

	
}
