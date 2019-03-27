package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {
    
  public FileHelper() {
  }

  public boolean make_directory(String directory_path) {

    Path path = Paths.get(directory_path);
    File file = new File(directory_path);

    if (!Files.exists(path)) {
        file.mkdirs();
        System.out.println("Directory created");
        return true;
    } else {
      System.out.println("Directory already exists");
      return true;
    }
  }

}