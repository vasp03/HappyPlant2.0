package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.UserRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.User;
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
