package com.example.pawfectmatch;

import java.io.Serializable;

public class Pet implements Serializable {
    String petName,bestHabit,Age,Gender,breed,size,description, phone, location, weight;
    String image,id;
    double Lat,Lng;
    boolean isLike;

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public Pet() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public Pet(String petName, String bestHabit, String age, String gender, String breed, String size, String description, String image, String phone, String weight, double Lat, double Lng,boolean isLike) {
        this.petName = petName;
        this.bestHabit = bestHabit;
        Age = age;
        Gender = gender;
        this.breed = breed;
        this.size = size;
        this.description = description;
        this.image=image;
        this.weight=weight;
        this.phone=phone;
        this.Lat=Lat;
        this.Lng=Lng;
        this.isLike=isLike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBestHabit() {
        return bestHabit;
    }

    public void setBestHabit(String bestHabit) {
        this.bestHabit = bestHabit;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
