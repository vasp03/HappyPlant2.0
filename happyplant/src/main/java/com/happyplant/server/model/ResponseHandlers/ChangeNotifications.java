package com.happyplant.server.model.ResponseHandlers;

import com.happyplant.server.model.IResponseHandler;
import com.happyplant.server.services.UserRepository;
import com.happyplant.shared.Message;
import com.happyplant.shared.User;

/**
 * Class that handles the change of the notifications
 */
public class ChangeNotifications implements IResponseHandler {
    private UserRepository userRepository;

    public ChangeNotifications(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        User user = request.getUser();
        Boolean notifications = request.getNotifications();
        if (userRepository.changeNotifications(user, notifications)) {
            response = new Message(true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}
