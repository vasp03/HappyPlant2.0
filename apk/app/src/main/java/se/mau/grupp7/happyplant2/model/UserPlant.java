package se.mau.grupp7.happyplant2.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class UserPlant {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private final int id;
    private final Plant plant; // The plant type
    private String nickName;
    private LocalDateTime lastWatered;
    private String category;
    private final LocalDateTime dateAdded;

    public UserPlant(Plant plant) {
        this.id = idGenerator.getAndIncrement();
        this.plant = plant;
        this.nickName = plant.getCommon_name(); // Default nickname
        this.lastWatered = LocalDateTime.now();
        this.dateAdded = LocalDateTime.now();
        this.category = "Unassigned";
    }

    public int getId() {
        return id;
    }

    public Plant getPlant() {
        return plant;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public LocalDateTime getLastWatered() {
        return lastWatered;
    }

    public String getLastWateredString() {
        return lastWatered.getYear() + "-" +
                lastWatered.getMonthValue() + "-" +
                lastWatered.getDayOfMonth() + " " +
                lastWatered.getHour() + ":" +
                (lastWatered.getMinute() < 10 ? "0" + lastWatered.getMinute() : lastWatered.getMinute());
    }

    public void water() {
        this.lastWatered = LocalDateTime.now();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public String getCommon_name() {
        return plant.getCommon_name();
    }

    public String getScientific_name() {
        return plant.getScientific_name();
    }

    public String getImageURL() {
        return plant.getImageURL();
    }
}
