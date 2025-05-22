package com.urmila.ecommerce.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long addressId;
    @NotBlank
    @Size(min=5,message="street name must contain 5 characters")
    private String street;
    @NotBlank
    @Size(min=5,message="building name must contain atleast 5 characters")
    private String buildingName;
    @NotBlank
    @Size(min=5,message="city name must contain atleast 4 characters")
    private String city;
    @NotBlank
    @Size(min=5,message="state name must contain atleast 4 characters")
    private String state;
    @NotBlank
    @Size(min=5,message="country name must contain atleast 4 characters")
    private String country;
    @NotBlank
    @Size(min=6,message="pincode name must contain atleast 6 characters")
    private String pincode;

    @ManyToMany(mappedBy = "addresses")
    private List<User> user=new ArrayList<>();

    public Address(String pincode, String country, String state, String city, String buildingName, String street, Long addressId) {
        this.pincode = pincode;
        this.country = country;
        this.state = state;
        this.city = city;
        this.buildingName = buildingName;
        this.street = street;
        this.addressId = addressId;
    }

    public Address() {

    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
