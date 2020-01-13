package ir.androidexception.andexmaphelper;

public class MarkerStatus {
    private String name;
    private Integer iconResourceId;

    public MarkerStatus(String name, Integer iconResourceId) {
        this.name = name;
        this.iconResourceId = iconResourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(Integer iconResourceId) {
        this.iconResourceId = iconResourceId;
    }
}
