package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileHelper {
    
  public FileHelper() {
  }

  /**
   * creates a new file directory given a directory path
   * @param directory_path
   * @return
   */
  public boolean makeDirectory(String directory_path) {
    Path path = Paths.get(directory_path);
    File file = new File(directory_path);

    if (!Files.exists(path)) {
        file.mkdirs();
        return true;
    } else {
      return true;
    }
  }

  /**
   * returns the file extension given a file name eg.".png"
   * @param
   * @return
   */
  public String getFileExtension(String f) {
    String ext = "";
    int i = f.lastIndexOf('.');
    if (i > 0 &&  i < f.length() - 1) {
      ext = f.substring(i + 1).toLowerCase();
    }
    return ext;
  }

  /**
   * returns true if file extension is of the correct type for photos
   * @param name
   * @return
   */
  public Boolean isValidFile(String name) {
    String extension = getFileExtension(name);
    return extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg");
  }


  /**
   * returns image filname with hashed name
   * @param name
   * @return
   */
  public String getHashedImage(String name) {

    UUID uuid = UUID.randomUUID();
    String randomUUIDString = uuid.toString();
    return randomUUIDString + "." + getFileExtension(name);
  }



}