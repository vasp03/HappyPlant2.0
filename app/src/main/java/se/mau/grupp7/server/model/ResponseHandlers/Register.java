package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.UserRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.User;

/**
 * Class that handles the procedure of a registration
 */
public class Register implements IResponseHandler {
    private UserRepository userRepository;

    public Register(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        User user = request.getUser();
        if (userRepository.saveUser(user)) {
            User savedUser = userRepository.getUserDetails(user.getEmail());
            response = new Message(savedUser, true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}
