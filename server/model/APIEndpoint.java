package se.mau.grupp7.server.model;

import io.javalin.http.Context;

public interface APIEndpoint {
    public String path();
    public String handle(Context ctx);
}
