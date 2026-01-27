package com.happyplant.server.model.ResponseHandlers;

import com.happyplant.server.model.IResponseHandler;
import com.happyplant.server.services.PlantRepository;
import com.happyplant.shared.Message;
import com.happyplant.shared.Plant;
import com.happyplant.shared.PlantDetails;
/**
 * Class that gets the plant details
 */
public class GetPlantDetails implements IResponseHandler {
    private PlantRepository plantRepository;

    public GetPlantDetails(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        Plant plant = request.getPlant();
        try {
            PlantDetails plantDetails = plantRepository.getPlantDetails(plant);
            response = new Message(plantDetails, true);
        } catch (Exception e) {
            response = new Message(false);
            e.printStackTrace();
        }
        return response;
    }
}
