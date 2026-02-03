package se.mau.grupp7.happyplant2.model;

import java.util.Date;

public class UserPlant {
    private final String name;
    private final String description;
    private final String imageURL;
    private final int wateringInterval;
    private final WaterAmount wateringAmount;
    private final Date lastTimeWatered;

    public UserPlant(String name, String description, String imageURL, int wateringInterval, WaterAmount wateringAmount, Date lastTimeWatered) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.wateringInterval = wateringInterval;
        this.wateringAmount = wateringAmount;
        this.lastTimeWatered = lastTimeWatered;
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

    public int getWateringInterval() {
        return wateringInterval;
    }

    public WaterAmount getWateringAmount() {
        return wateringAmount;
    }


    public Date getLastTimeWatered() {
        return lastTimeWatered;
    }
}
