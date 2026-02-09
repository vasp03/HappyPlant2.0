package se.mau.grupp7.server.controller;

import java.util.ArrayList;

import se.mau.grupp7.server.model.FlowerType;
import se.mau.grupp7.server.model.SunlightLevel;

public class Database {
    private ArrayList<FlowerType> flowerTypes;

    /**
     * TODO: Add database functionality using sqlite or similar.
     */
    public Database() {
        flowerTypes = new ArrayList<>();
    }

    /**
     * TODO: Change to get flowers from an api.
     * @return The created flower types
     */
    private ArrayList<FlowerType> addFlowerType() {
        flowerTypes.add(new FlowerType("Flower 1", "This is a flower", "https://example.com/flower1.jpg", 7, SunlightLevel.NONE, 10, 2));
        flowerTypes.add(new FlowerType("Flower 2", "This is another flower", "https://example.com/flower2.jpg", 7, SunlightLevel.DIRECT, 10, 2));
        flowerTypes.add(new FlowerType("Flower 3", "This is a third flower", "https://example.com/flower3.jpg", 7, SunlightLevel.INSIDE_THE_SUN, 10, 2));

        return addFlowersToDatabase();
    }

    /**
     * TODO: Change to get flowers from an api.
     * @return The created flower types
     */
    private ArrayList<FlowerType> addFlowersToDatabase() {
        return flowerTypes;
    }
}
