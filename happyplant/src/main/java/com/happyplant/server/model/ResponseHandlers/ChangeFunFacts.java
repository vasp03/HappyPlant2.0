package com.happyplant.server.model.ResponseHandlers;

import com.happyplant.server.model.IResponseHandler;
import com.happyplant.server.services.UserRepository;
import com.happyplant.shared.Message;
import com.happyplant.shared.User;
/**
 * Class that handles to change the fun facts
 */
public class ChangeFunFacts implements IResponseHandler {
    private UserRepository userRepository;

    public ChangeFunFacts (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param request
     * @return
     */
    @Override
    public Message getResponse(Message request) {
        Message response;
        User user = request.getUser();
        Boolean funFactsActivated = request.getNotifications();
        if (userRepository.changeFunFacts(user, funFactsActivated)) {
            response = new Message(true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}