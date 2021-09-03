package model;

import java.time.LocalDateTime;

/**
 * This model class creates the Country object
 */

public class Country {
    private int countryId;
    private String countryName;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUdatedBy;

    public Country(){

    }

    public Country(int id, String name){
        this.countryId = id;
        this.countryName = name;
        this.lastUdatedBy = User.getLoggedUser();
    }

    //setters

    public void setCountryId(int id) {
        this.countryId = id;
    }

    public void setCountryName(String name) {
        this.countryName = name;
    }

    public void setCreateDate(LocalDateTime createDate){
        this.createDate = createDate;
    }

    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUdatedBy(String lastUdatedBy) {
        this.lastUdatedBy = lastUdatedBy;
    }

    //getters

    public int getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastUdatedBy() {
        return lastUdatedBy;
    }

    @Override
    public String toString(){
        return (countryName);
    }
}
