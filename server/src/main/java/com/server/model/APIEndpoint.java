package com.server.model;

import io.javalin.http.Context;

public interface APIEndpoint {
    public String path();
    public void handle(Context context) throws UnsupportedOperationException;
} 
