package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.UserRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.User;
/**
 * Class that handles the procedure when a user logs in
 */
public class Login implements IResponseHandler {
    private UserRepository userRepository;

    public Login(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message getResponse(Message request) {
        Message response;
        String email = request.getUser().getEmail();
        String password = request.getUser().getPassword();
        if (userRepository.checkLogin(email, password)) {
            User user = userRepository.getUserDetails(email);
            response = new Message(user, true);
        } else {
            response = new Message(false);
        }
        return response;
    }
}
