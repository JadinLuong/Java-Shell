package driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Represents HTTPDataFetcher.
 * 
 * @author Ya-Tzu Wang
 */
public class HTTPDataFetcher implements RemoteDataFetcher {

  /**
   * Constructs a HTTPDataFetcher.
   */
  public HTTPDataFetcher() {}

  /**
   * Get remote data from HTTP URL
   * 
   * @param URL address of the file
   * @return content the content of the file
   * @throws IOException raised when URL is not valid or there is a problem with
   *         connection
   */
  @Override
  public String getRemoteDataFromURL(String URL) throws IOException {
    // read file given the address
    URL url = new URL(URL);
    BufferedReader input =
        new BufferedReader(new InputStreamReader(url.openStream()));
    String inputLine;
    // get content of the file line by line and store it
    String content = "";
    while ((inputLine = input.readLine()) != null) {
      content += inputLine + System.lineSeparator();
    }
    input.close();
    return content;
  }
}
