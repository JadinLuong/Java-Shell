package test;

import java.io.IOException;
import driver.RemoteDataFetcher;

/**
 * For testing command get.
 * 
 * @author Ya-Tzu Wang
 */
public class MockRemoteDataFetcher implements RemoteDataFetcher {

  /**
   * Constructs a mock object for remote data.
   */
  public MockRemoteDataFetcher() {}

  /**
   * Return content regardless of the URL given.
   */
  @Override
  public String getRemoteDataFromURL(String URL) throws IOException {
    if (URL.startsWith("http")) {
      // return content for testing
      return "Test Content";
    } else {
      // throw exception for invalid URL
      throw new IOException("");
    }
  }
}
