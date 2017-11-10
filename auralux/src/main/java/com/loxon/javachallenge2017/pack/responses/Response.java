package com.loxon.javachallenge2017.pack.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response
{

    @SerializedName("moveFrom")
    @Expose
    private Integer moveFrom;
    @SerializedName("moveTo")
    @Expose
    private Integer moveTo;
    @SerializedName("armySize")
    @Expose
    private Integer armySize;

    /**
     *
     * @param moveFrom
     * @param armySize
     * @param moveTo
     */
    public Response(Integer moveFrom, Integer moveTo, Integer armySize) {
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
        this.armySize = armySize;
    }

    public Integer getMoveFrom() {
        return moveFrom;
    }

    public void setMoveFrom(Integer moveFrom) {
        this.moveFrom = moveFrom;
    }

    public Integer getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(Integer moveTo) {
        this.moveTo = moveTo;
    }

    public Integer getArmySize() {
        return armySize;
    }

    public void setArmySize(Integer armySize) {
        this.armySize = armySize;
    }
}