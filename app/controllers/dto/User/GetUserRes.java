package controllers.dto.User;

import models.TravellerType;
import models.User;
import models.UserNationality;

import java.util.ArrayList;
import java.util.List;

public class GetUserRes {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private int dateOfBirth;
    private String gender;
    private List<NationalityRes> nationalities;
    private List<TravellerType> travellerTypes;
    private String userProfilePhoto;
    private Long defaultAlbumId;

    public GetUserRes(User user) {
        this.id = user.id;
        this.firstName = user.getFirstName();
        this.middleName = user.getMiddleName();
        this.lastName = user.getLastName();
        this.dateOfBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.travellerTypes = user.getTravellerTypes();
        this.setNationalities(user.getNationalities());
        this.userProfilePhoto = user.getUserProfilePhoto();
        this.defaultAlbumId = user.getDefaultAlbum().getId();

    }

    public void setNationalities(List<UserNationality> nationalities) {
        this.nationalities = new ArrayList<>();
        for (UserNationality nationality : nationalities) {
            this.nationalities.add(new NationalityRes(nationality));
        }
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public List<NationalityRes> getNationalities() {
        return nationalities;
    }

    public List<TravellerType> getTravellerTypes() {
        return travellerTypes;
    }

    public String getUserProfilePhoto() {return userProfilePhoto;}

    public Long getDefaultAlbumId() {
        return defaultAlbumId;
    }

}
