
package sample.pack.stateclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StationedArmy {

    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("size")
    @Expose
    private Integer size;

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

}