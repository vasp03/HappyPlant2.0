package com.happyplant.server.model.ResponseHandlers;

import com.happyplant.server.model.IResponseHandler;
import com.happyplant.server.services.UserPlantRepository;
import com.happyplant.shared.Message;
import com.happyplant.shared.Plant;
import com.happyplant.shared.User;
/**
 * Class that handles the chang of a nickname of a plant
 */
public class ChangeNickname implements IResponseHandler {
    private UserPlantRepository userPlantRepository;

    public ChangeNickname(UserPlantRepository userPlantRepository) {
        this.userPlantRepository = userPlantRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        User user = request.getUser();
        Plant plant = request.getPlant();
        String nickname = plant.getNickname();
        String newNickname = request.getNewNickname();
        if (userPlantRepository.changeNickname(user, nickname, newNickname)) {
            response = new Message(true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}
