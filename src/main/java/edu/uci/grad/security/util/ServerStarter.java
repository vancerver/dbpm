/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uci.grad.security.util;

import java.io.File;
/**
 *
 * @author vcagle
 */
public class ServerStarter {
    public static void main(String[] args){
	File serverFile;
	try {
	    serverFile = new File(new ServerStarter().getClass().getResource("/objectdb/server.exe").toURI());
	    Runtime.getRuntime().exec(serverFile.getAbsolutePath());
	} catch (Exception ex) {
	    ex.printStackTrace();
	} 
	System.exit(0);
    }
}
