package com.happyplant.server.model;

import com.happyplant.shared.Message;

/**
 * Interface makes sure all ResponseHandlers have at
 * least these methods
 */
public interface IResponseHandler {

    Message getResponse(Message request);
}

