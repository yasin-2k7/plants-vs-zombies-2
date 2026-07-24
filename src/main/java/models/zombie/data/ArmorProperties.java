package models.zombie.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ArmorProperties {
    @JsonProperty("aliases")
    private List<String> aliases;

    @JsonProperty("objclass")
    private String objclass;

    @JsonProperty("objdata")
    private ArmorData objdata;

    public List<String> getAliases() {
        return aliases;
    }

    public String getObjclass() {
        return objclass;
    }

    public ArmorData getObjdata() {
        return objdata;
    }
}
