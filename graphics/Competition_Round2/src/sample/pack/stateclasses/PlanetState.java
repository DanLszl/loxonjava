
package sample.pack.stateclasses;

import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanetState {

    private static final Random random = new Random(System.currentTimeMillis());

    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("ownershipRatio")
    @Expose
    private Double ownershipRatio;
    @SerializedName("stationedArmies")
    @Expose
    private List<StationedArmy> stationedArmies = null;
    @SerializedName("movingArmies")
    @Expose
    private List<MovingArmy> movingArmies = null;
    @SerializedName("planetID")
    @Expose
    private Integer planetID;

    //region Magic
    @SerializedName("magicNumber")
    @Expose
    private Double magicNumber = random.nextDouble();

    public static Random getRandom() {
        return random;
    }

    public Double getMagicNumber() {
        return magicNumber;
    }
    //endregion

    public void setMagicNumber(Double magicNumber) {
        this.magicNumber = magicNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Double getOwnershipRatio() {
        return ownershipRatio;
    }

    public void setOwnershipRatio(Double ownershipRatio) {
        this.ownershipRatio = ownershipRatio;
    }

    public List<StationedArmy> getStationedArmies() {
        return stationedArmies;
    }

    public void setStationedArmies(List<StationedArmy> stationedArmies) {
        this.stationedArmies = stationedArmies;
    }

    public List<MovingArmy> getMovingArmies() {
        return movingArmies;
    }

    public void setMovingArmies(List<MovingArmy> movingArmies) {
        this.movingArmies = movingArmies;
    }

    public Integer getPlanetID() {
        return planetID;
    }

    public void setPlanetID(Integer planetID) {
        this.planetID = planetID;
    }

}
