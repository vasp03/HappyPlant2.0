package com.happyplant.server.model.ResponseHandlers;

import com.happyplant.server.model.IResponseHandler;
import com.happyplant.server.services.UserPlantRepository;
import com.happyplant.shared.Message;
import com.happyplant.shared.User;

    /**
     * Class that handles to change all plants to watered
     */
    public class ChangeAllToWatered implements IResponseHandler {
        private UserPlantRepository userPlantRepository;

        public ChangeAllToWatered(UserPlantRepository userPlantRepository) {
            this.userPlantRepository = userPlantRepository;
        }

        @Override
        public Message getResponse(Message request) {
            Message response;
            User user = request.getUser();
            if (userPlantRepository.changeAllToWatered(user)) {
                response = new Message(true);
            } else {
                response = new Message(false);
            }
            return response;
        }
    }
