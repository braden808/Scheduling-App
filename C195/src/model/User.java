package model;

import java.time.LocalDateTime;

/**
 * This model class creates the User object
 */
public class User {
    private int userId;
    private String userName;
    private String password;
    private int active;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String updatedBy;
    private static String loggedUser;
    private static int loggedUserId;//keeps the logged in user info



    //setter methods

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public static void setLoggedUser(String loggedUser) {
        User.loggedUser = loggedUser;
    }

    public static void setLoggedUserId(int loggedUserId) {
        User.loggedUserId = loggedUserId;
    }

    //getter methods
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getActive() {
        return active;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public static String getLoggedUser() {
        return loggedUser;
    }

    public static int getLoggedUserId() {
        return loggedUserId;
    }

    @Override
    public String toString(){
        return userName;
    }
}