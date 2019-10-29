import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import tud.se.httpd.HttpConfig;
import tud.se.httpd.HttpException;
import tud.se.httpd.HttpServer;
import tud.se.httpd.HttpWorker;

/*
 * File: SimpleMain.java
 * Created on 01.12.2004 by: suesskraut
 *
 *
 */

/**
 * <p>Simple demonstration of how to compose the HttpServer and the HttpWorker
 * to a simple webserver.</p>
 * @author suesskraut
 * <pre>part of: tiny-http package: 
 * File: SimpleMain.java
 * created: 01.12.2004</pre>
 */
public class SimpleMain
{
	public static void main (String[] args) throws IOException, HttpException
	{
		// parse command line arguments
		HttpConfig config = new HttpConfig (args);
		// creating the worker 
		HttpWorker worker = new HttpWorker (config);
		// creating the server
		HttpServer server = new HttpServer (config);
		// connectiong the worker to the server
		server.setWorker (worker);
		// start the server
		server.start ();
	}
}
