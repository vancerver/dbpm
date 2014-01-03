/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uci.grad.security.util;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author vcagle
 */
public final class JavaProcess {

    private JavaProcess() {
    }

    public static int exec(Class klass) throws IOException,
	    InterruptedException {
	String javaHome = System.getProperty("java.home");
	String javaBin = javaHome
		+ File.separator + "bin"
		+ File.separator + "java";
	String classpath = System.getProperty("java.class.path");
	String className = klass.getCanonicalName();

	ProcessBuilder builder = new ProcessBuilder(
		javaBin, "-cp", classpath, className);

	Process process = builder.start();
	process.waitFor();
	return process.exitValue();
    }
}