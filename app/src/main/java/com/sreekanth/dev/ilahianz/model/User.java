package com.sreekanth.dev.ilahianz.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String username;
    private String id;
    private String imageURL;
    private String className;
    private String gender;
    private String Nickname, Category;
    private String search;
    private String status;
    private String lastSeen;
    private String email;
    private String Birthday;
    private String BirthYear;
    private String BirthMonth;
    private String Description;
    private String LastSeenPrivacy;
    private String LocationPrivacy;
    private String PhonePrivacy;
    private String EmailPrivacy;
    private String AboutPrivacy;
    private String ProfilePrivacy;
    private String PhoneNumber;
    private String BirthdayPrivacy;
    private String Latitude, Longitude;
    private String thumbnailURL;


    public User() {

    }

    public User(String username, String id, String imageURL, String className,
                String gender, String Nickname, String Category,
                String search, String status, String lastSeen,
                String email, String Birthday, String BirthYear,
                String BirthMonth, String Description, String LastSeenPrivacy,
                String LocationPrivacy, String PhonePrivacy, String EmailPrivacy,
                String AboutPrivacy, String ProfilePrivacy, String PhoneNumber,
                String Latitude, String Longitude, String BirthdayPrivacy, String thumbnailURL) {
        this.username = username;
        this.id = id;
        this.BirthdayPrivacy = BirthdayPrivacy;
        this.imageURL = imageURL;
        this.className = className;
        this.gender = gender;
        this.Nickname = Nickname;
        this.Category = Category;
        this.search = search;
        this.status = status;
        this.lastSeen = lastSeen;
        this.email = email;
        this.Birthday = Birthday;
        this.BirthYear = BirthYear;
        this.BirthMonth = BirthMonth;
        this.Description = Description;
        this.LastSeenPrivacy = LastSeenPrivacy;
        this.LocationPrivacy = LocationPrivacy;
        this.PhonePrivacy = PhonePrivacy;
        this.EmailPrivacy = EmailPrivacy;
        this.AboutPrivacy = AboutPrivacy;
        this.ProfilePrivacy = ProfilePrivacy;
        this.PhoneNumber = PhoneNumber;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.thumbnailURL = thumbnailURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getBirthdayPrivacy() {
        return BirthdayPrivacy;
    }

    public void setBirthdayPrivacy(String birthdayPrivacy) {
        BirthdayPrivacy = birthdayPrivacy;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLastSeenPrivacy() {
        return LastSeenPrivacy;
    }

    public void setLastSeenPrivacy(String lastSeenPrivacy) {
        LastSeenPrivacy = lastSeenPrivacy;
    }

    public String getLocationPrivacy() {
        return LocationPrivacy;
    }

    public void setLocationPrivacy(String locationPrivacy) {
        LocationPrivacy = locationPrivacy;
    }

    public String getPhonePrivacy() {
        return PhonePrivacy;
    }

    public void setPhonePrivacy(String phonePrivacy) {
        PhonePrivacy = phonePrivacy;
    }

    public String getEmailPrivacy() {
        return EmailPrivacy;
    }

    public void setEmailPrivacy(String emailPrivacy) {
        EmailPrivacy = emailPrivacy;
    }

    public String getAboutPrivacy() {
        return AboutPrivacy;
    }

    public void setAboutPrivacy(String aboutPrivacy) {
        AboutPrivacy = aboutPrivacy;
    }

    public String getProfilePrivacy() {
        return ProfilePrivacy;
    }

    public void setProfilePrivacy(String profilePrivacy) {
        ProfilePrivacy = profilePrivacy;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getBirthYear() {
        return BirthYear;
    }

    public void setBirthYear(String birthYear) {
        this.BirthYear = birthYear;
    }

    public String getBirthMonth() {
        return BirthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.BirthMonth = birthMonth;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        this.Birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }
}
