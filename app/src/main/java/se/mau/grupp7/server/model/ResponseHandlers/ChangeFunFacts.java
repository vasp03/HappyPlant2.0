package se.mau.grupp7.server.model.ResponseHandlers;

import se.mau.grupp7.server.model.IResponseHandler;
import se.mau.grupp7.server.services.UserRepository;
import se.mau.grupp7.happyplant2.shared.Message;
import se.mau.grupp7.happyplant2.shared.User;
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