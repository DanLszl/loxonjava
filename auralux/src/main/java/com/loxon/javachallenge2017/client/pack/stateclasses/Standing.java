
package com.loxon.javachallenge2017.client.pack.stateclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Standing {

    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("strength")
    @Expose
    private Integer strength;
    @SerializedName("userID")
    @Expose
    private String userID;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
