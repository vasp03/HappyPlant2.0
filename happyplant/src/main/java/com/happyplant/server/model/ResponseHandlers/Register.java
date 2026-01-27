package com.happyplant.server.model.ResponseHandlers;

import com.happyplant.server.model.IResponseHandler;
import com.happyplant.server.services.UserRepository;
import com.happyplant.shared.Message;
import com.happyplant.shared.User;
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
