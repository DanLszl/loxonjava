
package com.loxon.javachallenge2017.pack.stateclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovingArmy {

    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("x")
    @Expose
    private Double x;
    @SerializedName("y")
    @Expose
    private Double y;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

}
