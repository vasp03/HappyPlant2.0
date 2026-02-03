package com.server.model;

import com.server.controller.Controller;

import io.javalin.http.Context;

public interface APIEndpoint {
    public void setController(Controller controller);
    public String path();
    public void handle(Context context) throws UnsupportedOperationException;
} 
