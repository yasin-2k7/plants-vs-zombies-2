package models.zombie.data;

import java.util.List;

public class ArmorProperties {
    private List<String> aliases;
    private String objclass;
    private ArmorData objdata;

    public List<String> getAliases() { return aliases; }
    public String getObjclass() { return objclass; }
    public ArmorData getObjdata() { return objdata; }
}
