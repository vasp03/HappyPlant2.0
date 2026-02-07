package se.mau.grupp7.happyplant2.model;

import java.util.Date;

public class Plant {
    private int id;
    private String common_name;
    private String scientific_name;
    private String genus;
    private String family;
    private final String description;
    private final String imageURL;
    private final WaterAmount wateringAmount;
    private Date lastWatered;
    private final int wateringInterval;

    public Plant(
            int id,
            String common_name,
            String scientific_name,
            String genus,
            String family,
            String description,
            String imageURL,
            WaterAmount wateringAmount,
            Date lastWatered,
            int wateringInterval
    ) {
        this.id = id;
        this.common_name = common_name;
        this.scientific_name = scientific_name;
        this.genus = genus;
        this.family = family;
        this.description = description;
        this.imageURL = imageURL;
        this.wateringAmount = wateringAmount;
        this.lastWatered = lastWatered;
        this.wateringInterval = wateringInterval;
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

    public String getFamily() {
        return family;
    }


    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public WaterAmount getWateringAmount() {
        return wateringAmount;
    }

    public Date getLastWatered() {
        return lastWatered;
    }

    public int getWateringInterval() {
        return wateringInterval;
    }

    public Plant setLastWatered(Date lastWatered) {
        this.lastWatered = lastWatered;
        return this;
    }
}
