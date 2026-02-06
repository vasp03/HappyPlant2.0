package se.mau.grupp7.happyplant2.model;

import java.io.Serializable;

/**
 * Container class for more detailed information about a plant
 * Created by: Frida Jacobsson
 * Updated by:
 **/
public class PlantDetails implements Serializable {
    private int id;
    private String common_name;
    private String scientific_name;
    private String genus;
    private String imageUrl;

    public PlantDetails(int id, String common_name, String scientific_name, String genus, String imageUrl) {
        this.id = id;
        this.common_name = common_name;
        this.scientific_name = scientific_name;
        this.genus = genus;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getCommon_name() {
        return common_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public String getGenus() {
        return genus;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
