package sample.pack.magic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MagicValue {

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
