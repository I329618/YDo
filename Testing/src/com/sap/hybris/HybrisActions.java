package com.sap.hybris;

import java.io.OutputStream;

public class HybrisActions {
	public static void AntCleanAll() {
		try {
			// Execute command
			String command = "cmd /c start C:/mock/hybris/Testing/antcleanall.bat";
			System.out.println(command);
			Process child = Runtime.getRuntime().exec(command);
			// Get output stream to write from it
			OutputStream out = child.getOutputStream();
			out.write("antall.bat".getBytes());
			out.flush();
			out.close();
			Thread.sleep(240000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void AntCleanAllAndServerStart() {
		try {
			// Execute command
			String command = "cmd /c start antallandserverstart.bat";
			Process child = Runtime.getRuntime().exec(command);
			// Get output stream to write from it
			OutputStream out = child.getOutputStream();
			out.write("antall.bat".getBytes());
			out.flush();
			out.close();
			Thread.sleep(240000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
