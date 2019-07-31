package controllers.dto.User;

import models.TravellerType;
import models.User;
import models.UserNationality;

import java.util.ArrayList;
import java.util.List;

public class GetOwnUserRes {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private int dateOfBirth;
    private String gender;
    private List<NationalityRes> nationalities;
    private List<TravellerType> travellerTypes;
    private String email;
    private int accountType;
    private String userProfilePhoto;
    private Long defaultAlbumId;

    public GetOwnUserRes(User user) {
        this.id = user.id;
        this.firstName = user.firstName;
        this.middleName = user.middleName;
        this.lastName = user.lastName;
        this.dateOfBirth = user.dateOfBirth;
        this.gender = user.gender;
        this.travellerTypes = user.travellerTypes;
        this.setNationalities(user.nationalities);
        this.email = user.email;
        this.accountType = user.accountType;
        this.userProfilePhoto = user.userProfilePhoto;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getUserProfilePhoto() {return userProfilePhoto;}

    public Long getDefaultAlbumId() {
        return defaultAlbumId;
    }
}
