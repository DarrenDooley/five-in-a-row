package com.genesys.darrendooley;

import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1")
@Api(value = "/v1", description = "five-in-a-row endpoints")
@Produces(MediaType.TEXT_PLAIN)
public class RequestHandler {

    private static final String NAME_NOT_FOUND = "Player '%s' not in game.";
    private static final  String WAIT_FOR_PLAYER = "Please wait for second player to join.";
    private static final String GAME_FULL = "Sorry, the game is full.";

    Grid grid;

    public RequestHandler() {
        this.grid = new Grid();
    }

    @GET
    @Path("/state")
    public Response state(@QueryParam("name") String name) {
        name = name.toLowerCase();
        if (!grid.doesPlayerExist(name)) {
            return Response.ok(String.format(NAME_NOT_FOUND, name)).build();
        }
        if (!grid.hasTwoPlayers() && !grid.isGameOver()) {
            return Response.ok(WAIT_FOR_PLAYER).build();
        }
        String state = grid.getState(name);
        return Response.ok(state).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/move")
    public Response move(@FormParam("name") String name, @FormParam("columnNum") int columnNum) {
        name = name.toLowerCase();
        if (!grid.doesPlayerExist(name)) {
            return Response.ok(String.format(NAME_NOT_FOUND, name)).build();
        }
        if (!grid.hasTwoPlayers() && !grid.isGameOver()) {
            return Response.ok(WAIT_FOR_PLAYER).build();
        }
        String responseString = grid.dropDisc(name, columnNum);
        return Response.ok(responseString).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/join")
    public Response join(@FormParam("name") String name) {
        name = name.toLowerCase();
        if (grid.hasTwoPlayers() || grid.isGameOver()) {
            return Response.ok(GAME_FULL).build();
        }
        String responseString = grid.addPlayer(name);
        return Response.ok(responseString).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/leave")
    public Response leave(@FormParam("name") String name) {
        name = name.toLowerCase();
        if (!grid.doesPlayerExist(name)) {
            return Response.ok(String.format(NAME_NOT_FOUND, name)).build();
        }
        String responseString = grid.removePlayer(name);
        if (!grid.hasAnyPlayers()) {
            grid = new Grid();
        }
        return Response.ok(responseString).build();
    }
}