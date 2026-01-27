package com.happyplant.server.model.ResponseHandlers;

import com.happyplant.server.model.IResponseHandler;
import com.happyplant.server.services.UserPlantRepository;
import com.happyplant.shared.Message;
import com.happyplant.shared.Plant;
import com.happyplant.shared.User;

import java.util.ArrayList;
/**
 * Class that gets the users library
 */
public class GetLibrary implements IResponseHandler {
    private UserPlantRepository userPlantRepository;

    public GetLibrary(UserPlantRepository userPlantRepository) {
        this.userPlantRepository = userPlantRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        User user = request.getUser();
        try {
            ArrayList<Plant> userLibrary = userPlantRepository.getUserLibrary(user);
            response = new Message(userLibrary, true);
        } catch (Exception e) {
            response = new Message(false);
            e.printStackTrace();
        }
        return response;
    }
}
