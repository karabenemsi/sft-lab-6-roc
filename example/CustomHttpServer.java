import java.util.concurrent.BlockingQueue;

import tud.se.httpd.HttpConfig;
import tud.se.httpd.HttpServer;
import tud.se.httpd.HttpWorker;

public class CustomHttpServer extends HttpServer {

  private HttpConfig httpConfig;
  private BlockingQueue<Runnable> queue;
  private HttpWorker worker;

  public CustomHttpServer(HttpConfig config, BlockingQueue<Runnable> queue) {
    super(config);
    this.httpConfig = config;
    this.queue = queue;
  }

  @Override
  protected void afterHandlingRequest() {
    // Worker dies after 10 requests so restart it after 10 requests
    if (worker.getRequestNum() >= 10) {
      this.restartWorker();
    }
    // Server dies after 15 requests so restart it after 15 requests
    if (super.getReceivedRequests() >= 15) {
      try {
        // Put the restartServer task into the queue to be executed by the main thread
        this.queue.put(() -> {

          HttpWorker worker = this.getWorker();
          this.halt();
          // Create a new server and start it
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

  public void setWorker(HttpWorker worker) {
    super.setWorker(worker);
    this.worker = worker;
  }

  public HttpWorker getWorker() {
    return this.worker;
  }

}
