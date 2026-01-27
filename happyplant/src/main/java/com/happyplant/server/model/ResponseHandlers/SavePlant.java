package com.happyplant.server.model.ResponseHandlers;

import com.happyplant.server.model.IResponseHandler;
import com.happyplant.server.services.UserPlantRepository;
import com.happyplant.shared.Message;
import com.happyplant.shared.Plant;
import com.happyplant.shared.User;
/**
 * Class that saved a users plant
 */
public class SavePlant implements IResponseHandler {
    private UserPlantRepository userPlantRepository;

    public SavePlant(UserPlantRepository userPlantRepository) {
        this.userPlantRepository = userPlantRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        User user = request.getUser();
        Plant plant = request.getPlant();
        if (userPlantRepository.savePlant(user, plant)) {
            response = new Message(true);

        } else {
            response = new Message(false);
        }
        return response;
    }
}
