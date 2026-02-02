package se.mau.grupp7.server.model;

import se.mau.grupp7.happyplant2.shared.Message;

/**
 * Interface makes sure all ResponseHandlers have at
 * least these methods
 */
public interface IResponseHandler {

    Message getResponse(Message request);
}

