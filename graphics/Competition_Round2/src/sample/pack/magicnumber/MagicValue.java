package sample.pack.magicnumber;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MagicValue {

    public MagicValue() {

    }

    public MagicValue(Integer planetId, Double magicness) {
        this.planetIndex = planetId;
        this.magicness = magicness;
    }

    @SerializedName("planetIndex")
    @Expose
    private Integer planetIndex;

    @SerializedName("magicness")
    @Expose
    private Double magicness;

    public Integer getPlanetIndex() {
        return planetIndex;
    }

    public void setPlanetIndex(Integer planetIndex) {
        this.planetIndex = planetIndex;
    }

    public Double getMagicness() {
        return magicness;
    }

    public void setMagicness(Double magicness) {
        this.magicness = magicness;
    }

    @Override
    public String toString() {
        return "MagicValue{" +
                "planetIndex=" + planetIndex +
                ", magicness=" + magicness +
                '}';
    }
}
