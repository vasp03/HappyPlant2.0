package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.UserPlantRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.User;

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
