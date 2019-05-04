package utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {
    
  public FileHelper() {
  }

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


  public String getFileExtension(String f) {
    String ext = "";
    int i = f.lastIndexOf('.');
    if (i > 0 &&  i < f.length() - 1) {
      ext = f.substring(i + 1).toLowerCase();
    }
    return ext;
  }


  public String getHashedImage(String name) {
    return name.hashCode() + "." + getFileExtension(name);
  }


}