package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.UserRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.User;

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
