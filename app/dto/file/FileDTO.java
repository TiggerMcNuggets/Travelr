package dto.file;

import dto.user.UserSimpleDTO;
import models.File;

public class FileDTO {

    public UserSimpleDTO user;

    public String name;

    public String filepath;

    public String extension;

    public FileDTO(File file) {
        this.name = file.getName();
        this.filepath = file.getFilepath();
        this.extension = file.getExtension();
        this.user = new UserSimpleDTO(file.getUser());
    }
}
