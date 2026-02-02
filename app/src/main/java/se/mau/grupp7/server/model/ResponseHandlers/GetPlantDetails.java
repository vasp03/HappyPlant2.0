package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.PlantRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.Plant;
import se.mau.grupp7.happyplant2.shared.PlantDetails;
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
