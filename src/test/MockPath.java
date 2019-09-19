package test;

import java.util.ArrayList;
import driver.Directory;
import driver.File;
import driver.InvalidPathException;

public class MockPath {

  private Directory root;
  
  private Directory currentDirectory;
  
  private static final String SEPARATOR = "/";
  
  public MockPath(MockJFileSystem fs) {
    this.root = fs.getRoot();
    this.currentDirectory = fs.getCurrentDirectory();
  }
  
  public File searchByPath(String path) throws InvalidPathException {
    // get an array of the file names that we wish to find in the fileSystem
    ArrayList<String> targetFileNames = this.RetrieveFileNames(path);
    // determine to start at root or current directory
    File file = this.getStartingDirectory(path);
    // get a list of existing files from starting directory
    ArrayList<File> actualFiles = ((Directory) file).getContents();
    for (int i = 0; i < targetFileNames.size(); i++) {
      String name = targetFileNames.get(i);
      // get parent if given ..
      if (name.equals("..")) {
        file = file.getParentDir();
        if (file == null) {
          file = this.root;
        }
        // if target file name is not one of "..", ".", and "", search for it
      } else {
        file = this.findFileWithName(actualFiles, name);
      }
      // advance to next level
      if (file instanceof Directory) {
        actualFiles = ((Directory) file).getContents();
      }
    }
    return file;
  }
  
  private Directory getStartingDirectory(String path) {
    // if absolute path (starting at root)
    Directory startingDirectory = this.root;
    // if relative path
    if (!(path.startsWith(this.root.getFileName()))) {
      startingDirectory = this.currentDirectory;
    }
    return startingDirectory;
  }
  
  private File findFileWithName(ArrayList<File> files, String name)
      throws InvalidPathException {
    for (int index = 0; index < files.size(); index++) {
      File file = files.get(index);
      if (file.getFileName().equals(name)) {
        return file;
      }
    }
    // at this point, it's clear the File with given fileName does not exist
    throw new InvalidPathException("File with this path does not exist.");
  }
  
  private ArrayList<String> RetrieveFileNames(String path) {
    path = path.trim();
    String[] fileNames = path.split(SEPARATOR);
    ArrayList<String> result = new ArrayList<String>();
    for (String name : fileNames) {
      name = name.trim();
      if ((!(name.equals(""))) && (!(name.equals(".")))) {
        result.add(name);
      }
    }
    return result;
  }
}
