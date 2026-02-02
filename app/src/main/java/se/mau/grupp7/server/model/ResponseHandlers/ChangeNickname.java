package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.UserPlantRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.Plant;
import se.mau.grupp7.happyplant2.shared.User;
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
