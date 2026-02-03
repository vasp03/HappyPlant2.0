package se.mau.grupp7.happyplant2.model;

public class PlantType {
    private final String name;

    private String scientificName;
    private int light;
    private String family;
    private final String description;
    private final String imageURL;

    public PlantType(String name, String description, String imageURL){
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }
}
