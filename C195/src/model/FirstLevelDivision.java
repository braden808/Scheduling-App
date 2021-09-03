package model;

import java.time.LocalDateTime;

public class FirstLevelDivision {
    private int divisionId;
    private String divisionName;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryId;

    /**
     * This model class creates the FirstLevelDivision object
     */
    public FirstLevelDivision(String divisionName, int countryId){
        this.divisionName = divisionName;
        this.createDate = LocalDateTime.now();
        this.lastUpdate = LocalDateTime.now();
        this.lastUpdatedBy = User.getLoggedUser();
        this.countryId = countryId;
    }

    public FirstLevelDivision() {

    }
    //getters

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public int getCountryId() {
        return countryId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    //setters

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setDivisionName(String division) {
        this.divisionName = division;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString(){
        return (divisionName);
    }
}
