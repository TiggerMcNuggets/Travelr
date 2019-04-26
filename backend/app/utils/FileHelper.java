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

}