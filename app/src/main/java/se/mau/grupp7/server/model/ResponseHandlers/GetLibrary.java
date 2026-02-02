package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.UserPlantRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.Plant;
import se.mau.grupp7.happyplant2.shared.User;

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
