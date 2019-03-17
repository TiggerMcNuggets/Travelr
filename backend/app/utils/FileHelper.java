package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {
  private static final String FOLDER = "F:\\nikos7\\Desktop\\testFiles\\Afolder\\inA\\inB\\inC";

  public FileHelper() {
  }

  public boolean make_directory(String directory_path) {

    Path path = Paths.get(directory_path);
    File file = new File(directory_path);

    if (!Files.exists(path)) {
//      try {
//        Files.createDirectory(path);
        file.mkdirs();
        System.out.println("Directory created");
        return true;
//      }
//      catch (IOException e) {
//        System.out.println("Cannot create directory");
//        return false;
//      }
    } else {

      System.out.println("Directory already exists");
      return true;
    }
  }

}