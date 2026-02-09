package se.mau.grupp7.happyplant2.model;

import android.graphics.Color;

import java.time.LocalDateTime;

public class Plant {
    private final int id;
    private final String common_name;
    private final String scientific_name;
    private final String genus;
    private final String family;
    private final String description;
    private final String imageURL;
    private final WaterAmount wateringAmount;
    private LocalDateTime lastWatered;
    private final int wateringInterval;
    private final LocalDateTime dateAdded;
    private String category;
    private int plantLength;
    private String comment;
    private String pictureUrl;
    private String plantPot;
    private String extraFacts;
    private String nickName;

    public Plant(
            int id,
            String common_name,
            String scientific_name,
            String genus,
            String family,
            String description,
            String imageURL,
            WaterAmount wateringAmount,
            LocalDateTime lastWatered,
            int wateringInterval,
            String category,
            int plantLength,
            String comment,
            String pictureUrl,
            String plantPot,
            String extraFacts,
            String nickName
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
        this.dateAdded = LocalDateTime.now();
        this.category = category;
        this.plantLength = plantLength;
        this.comment = comment;
        this.pictureUrl = pictureUrl;
        this.plantPot = plantPot;
        this.extraFacts = extraFacts;
        this.nickName = nickName;
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

    public String getLastWatered() {

        return lastWatered.getYear() + "-" +
                lastWatered.getMonthValue() + "-" +
                lastWatered.getDayOfMonth() + " " +
                lastWatered.getHour() + ":" +
                (lastWatered.getMinute() < 10 ? "0" + lastWatered.getMinute() : lastWatered.getMinute());
    }

    public LocalDateTime getLastWateredDateTime() {
        return lastWatered;
    }

    public int getWateringInterval() {
        return wateringInterval;
    }

    public LocalDateTime getDateAdded() { return dateAdded; }

    public String getCategory() {
        return category;
    }

    /**
     * Get the length of the plant in mm
     */
    public int getPlantLength() {
        return plantLength;
    }

    public String getComment() {
        return comment;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getPlantPot() {
        return plantPot;
    }

    public String getExtraFacts() {
        return extraFacts;
    }

    public String getNickName() {
        return nickName;
    }

    public void setCategory(String category) {
        this.category = (category != null) ? category : "";
    }

    public Plant water() {
        this.lastWatered = LocalDateTime.now();
        return this;
    }

    public int getWaterStatus() {
        LocalDateTime now = LocalDateTime.now();

        if (now.minusDays(wateringInterval).isBefore(lastWatered)) {
            return Color.RED;
        } else {
            return Color.GREEN;
        }
    }
}
