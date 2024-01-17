import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import tud.se.httpd.HttpConfig;
import tud.se.httpd.HttpException;
import tud.se.httpd.HttpServer;
import tud.se.httpd.HttpWorker;

/**
 * <p>
 * Simple demonstration of how to compose the HttpServer and the HttpWorker
 * to a simple webserver.
 * </p>
 *
 * @author
 * 
 *         <pre>
 * part of: tiny-http package:
 * File: SimpleMain.java
 *         </pre>
 */
public class SimpleMain {
	// queue for the tasks
	private static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

	public static void main(String[] args) throws IOException, HttpException, InterruptedException {
		// parse command line arguments
		HttpConfig config = new HttpConfig(args);
		// creating the worker
		HttpWorker worker = new HttpWorker(config);
		// creating the server
		HttpServer server = new CustomHttpServer(config, queue);
		// connectiong the worker to the server
		server.setWorker(worker);
		// start the server
		server.start();

		while (true) {
			// wait for a task/restart request and execute it
			Runnable task = queue.take();
			task.run();
		}
	}

}
