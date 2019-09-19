package driver;

import java.util.ArrayList;
import java.io.*;

/**
 * For command get.
 * 
 * @author Ya-Tzu Wang
 */
public class Get extends Command {

  /**
   * The current directory of the fileSystem
   */
  private Directory current;

  /**
   * The remote data fetch
   */
  private RemoteDataFetcher dataFetcher;

  /**
   * Construct Get with the file system for access to the current directory.
   * 
   * @param fs the file system
   */
  public Get(FileSystem<Directory> fs, RemoteDataFetcher dataFetcher) {
    super(false);
    this.current = fs.getCurrentDirectory();
    this.dataFetcher = dataFetcher;
  }

  /**
   * Execute command get.
   * 
   * @param arguments a list containing URLs
   */
  public void executeCommand(ArrayList<String> arguments) {
    // if there is no url following command get
    if (arguments.size() == 0) {
      this.setError("get: missing URL");
    } else {
      // for each URL, retrieve file content and store it in current directory
      for (int index = 0; index < arguments.size(); index++) {
        String URL = arguments.get(index);
        try {
          String content = dataFetcher.getRemoteDataFromURL(URL);;
          retrieve(URL, content);
          if (index != arguments.size() - 1) {
            this.setPrintCommand(System.lineSeparator());
          }
        } catch (IOException e3) {
          this.setError("get: problem with URL");
        }
      }
    }
  }

  /**
   * Retrieve content of the file from the given URL and store it in the current
   * directory.
   * 
   * @param address the URL where file resides
   * @throws IOException raised when url is not valid or there is a problem with
   *         connection
   */
  public void retrieve(String address, String content) throws IOException {
    // create a file name for the new file
    String fileName = address.substring(address.lastIndexOf('/') + 1,
        address.lastIndexOf('.'));
    // create a new file
    TextFile newFile = new TextFile(fileName, this.current, content);
    // add file to be a child for the current directory
    boolean done = false;
    int index = 1;
    while (!done) {
      try {
        this.current.addContents(newFile);
        done = true;
        // change file name if file name contain invalid characters
      } catch (InvalidFileNameException e) {
        newFile.setFileName("newFile" + "_" + index);
        index++;
        done = false;
        // change file name if the previous file name already exists
      } catch (ExistingFileException e) {
        newFile.setFileName(fileName + "_" + index);
        index++;
        done = false;
      }
    }
    this.setPrintCommand(
        "get: file from " + address + " is named " + newFile.getFileName()
            + " and stored in " + newFile.getParentDir().getFileName());
  }
}
