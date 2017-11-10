
package com.loxon.javachallenge2017.pack.descriptionclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player {

    @SerializedName("raceID")
    @Expose
    private Integer raceID;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userID")
    @Expose
    private String userID;

    public Integer getRaceID() {
        return raceID;
    }

    public void setRaceID(Integer raceID) {
        this.raceID = raceID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
