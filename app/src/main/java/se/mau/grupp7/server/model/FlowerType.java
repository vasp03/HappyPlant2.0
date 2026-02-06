package se.mau.grupp7.server.model;

public class FlowerType {
    private final String name;
    private final String description;
    private final String imageURL;
    /**
     * How many days between watering
     */
    private final int wateringInterval;
    private final SunlightLevel sunlightLevel;
    private final int minTemp;
    /**
     * Max temp is at least min temp.
     */
    private final int maxTemp;

    public FlowerType(String name, String description, String imageURL, int wateringInterval, SunlightLevel sunlightLevel, int minTemp, int maxTemp) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.wateringInterval = wateringInterval;
        this.sunlightLevel = sunlightLevel;
        this.minTemp = minTemp;
        this.maxTemp = Math.max(maxTemp, minTemp);
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

    public SunlightLevel getSunlightLevel() {
        return sunlightLevel;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }
}
