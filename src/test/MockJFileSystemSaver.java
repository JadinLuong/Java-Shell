package test;

import driver.Directory;
import driver.FileSystem;
import driver.FileSystemSaver;

public class MockJFileSystemSaver implements FileSystemSaver {

  private String fileContent;

  public MockJFileSystemSaver() {
    fileContent = "";
  }

  @Override
  public String saveFileSystem(String fileName,
      FileSystem<Directory> fileSystem) {
    fileContent = fileName + ": saved to fileSystem";
    return fileContent;
  }

  @Override
  public String getSavedFileSystem(String fileName,
      FileSystem<Directory> fileSystem) {

    String errors = "";

    if (fileName.equals("FileNotFoundException")) {
      errors = fileName + ": file not found";
    } else {
      ((MockJFileSystem) fileSystem).addTextFile(fileName, "/",
          "system content");
    }

    return errors;
  }


}
