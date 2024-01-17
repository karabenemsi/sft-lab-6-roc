import java.util.concurrent.BlockingQueue;

import tud.se.httpd.HttpConfig;
import tud.se.httpd.HttpServer;
import tud.se.httpd.HttpWorker;

public class CustomHttpServer extends HttpServer {

  private HttpConfig httpConfig;
  private BlockingQueue<Runnable> queue;

  public CustomHttpServer(HttpConfig config, BlockingQueue<Runnable> queue) {
    super(config);
    this.httpConfig = config;
    this.queue = queue;
  }

  @Override
  protected void afterHandlingRequest() {
    // Worker dies after 10 requests so restart it after 10 requests
    if (super.getReceivedRequests() % 10 == 0) {
      this.restartWorker();
    }
    // Server dies after 15 requests so restart it after 15 requests
    if (super.getReceivedRequests() >= 15) {
      try {
        // Put the restartServer task into the queue to be executed by the main thread
        this.queue.put(() -> {
          this.halt();
          // Create a new worker and server and start them
          HttpWorker worker = new HttpWorker(this.httpConfig);
          CustomHttpServer newServer = new CustomHttpServer(this.httpConfig, queue);
          newServer.setWorker(worker);
          newServer.start();
        });
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void restartWorker() {
    System.out.println("restartWorker");
    HttpWorker worker = new HttpWorker(this.httpConfig);
    this.setWorker(worker);
  }

}
