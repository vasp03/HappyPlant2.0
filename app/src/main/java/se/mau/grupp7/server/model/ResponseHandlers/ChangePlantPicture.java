package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.UserPlantRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.Plant;
import se.mau.grupp7.happyplant2.shared.User;

public class ChangePlantPicture implements IResponseHandler {

    private UserPlantRepository userPlantRepository;

    public ChangePlantPicture(UserPlantRepository userPlantRepository) {
        this.userPlantRepository = userPlantRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        User user = request.getUser();
        Plant plant = request.getPlant();
        if (userPlantRepository.changePlantPicture(user, plant)) {
            response = new Message(true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}
