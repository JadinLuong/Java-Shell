package driver;

import java.io.IOException;

/**
 * Represents an interface of a RemoteDataFetcher.
 * 
 * @author Ya-Tzu Wang
 */
public interface RemoteDataFetcher {

  /**
   * Get data from the given URL.
   * 
   * @param URL
   * @return data residing in given URL
   */
  public String getRemoteDataFromURL(String URL) throws IOException;
}
