package ex20;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class WebsideStatus extends Thread {

    public WebsideStatus(BlockingQueue blockingQueue) {
        blockingString = blockingQueue;
    }
    BlockingQueue<String> blockingString;
    String url;
    boolean listEmpty = false;

    public void run() {
        while (!listEmpty) {
            String result = "Error";
            try {
                url = blockingString.poll(3, TimeUnit.SECONDS);

                if (url != null) {
                    URL siteURL = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) siteURL
                            .openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    int code = connection.getResponseCode();
                    if (code == 200) {
                        result = "Green";
                    }
                    if (code == 301) {
                        result = "Redirect";
                    }
                    System.out.println(url + "\t\tStatus:" + result);
                } else {
                    listEmpty = true;
                }
            } catch (Exception e) {
                result = "->Red<-";

            }

        }
    }
}
