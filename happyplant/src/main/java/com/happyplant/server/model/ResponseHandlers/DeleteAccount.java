package com.happyplant.server.model.ResponseHandlers;

import com.happyplant.server.model.IResponseHandler;
import com.happyplant.server.services.UserRepository;
import com.happyplant.shared.Message;
import com.happyplant.shared.User;
/**
 * Class that handles the changes when the user want to delete an account
 */
public class DeleteAccount implements IResponseHandler {
    private UserRepository userRepository;

    public DeleteAccount(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        User userToDelete = request.getUser();
        if (userRepository.deleteAccount(userToDelete.getEmail(), userToDelete.getPassword())) {
            response = new Message(true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}
