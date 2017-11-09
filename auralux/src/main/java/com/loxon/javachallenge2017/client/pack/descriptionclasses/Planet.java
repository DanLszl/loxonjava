
package com.loxon.javachallenge2017.client.pack.descriptionclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Planet {

    @SerializedName("x")
    @Expose
    private Integer x;
    @SerializedName("y")
    @Expose
    private Integer y;
    @SerializedName("planetID")
    @Expose
    private Integer planetID;
    @SerializedName("radius")
    @Expose
    private Integer radius;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getPlanetID() {
        return planetID;
    }

    public void setPlanetID(Integer planetID) {
        this.planetID = planetID;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

}
