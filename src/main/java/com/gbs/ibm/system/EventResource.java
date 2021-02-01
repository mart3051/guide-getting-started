package com.gbs.ibm.system;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@RequestScoped
@Path("/events")
public class EventResource {
    @Inject
    private EventDAO eventDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonArray getEvents() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder finalArray = Json.createArrayBuilder();
        for (Event event : eventDAO.readAllEvents()) {
            builder.add("name", event.getName()).add("time", event.getTime())
                   .add("location", event.getLocation()).add("id", event.getId());
            finalArray.add(builder.build());
        }
        return finalArray.build();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonObject getEvent(@PathParam("id") int eventId) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Event event = eventDAO.readEvent(eventId);
        if(event != null) {
            builder.add("name", event.getName()).add("time", event.getTime())
                .add("location", event.getLocation()).add("id", event.getId());
        }
        return builder.build();
    }

    /**
     * This method returns the existing/stored events in Json format
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addNewEvent(@RequestBody EvenWrapper event) {
        Event newEvent = new Event(event.getName(),event.getLocation(),event.getTime());
        if(!eventDAO.findEvent(event.getName(),event.getLocation(),event.getTime()).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Event already exists").build();
        }
        eventDAO.createEvent(newEvent);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * This method updates a new event from the submitted data (name, time and
     * location) by the user.
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateEvent(@RequestBody EvenWrapper event,
        @PathParam("id") int id) {
        Event prevEvent = eventDAO.readEvent(id);
        if(prevEvent == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Event does not exist").build();
        }
        if(!eventDAO.findEvent(event.getName(), event.getLocation(),event.getTime()).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Event already exists").build();
        }
        prevEvent.setName(event.getName());
        prevEvent.setLocation(event.getLocation());
        prevEvent.setTime(event.getTime());

        eventDAO.updateEvent(prevEvent);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * This method deletes a specific existing/stored event
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteEvent(@PathParam("id") int id) {
        Event event = eventDAO.readEvent(id);
        if(event == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Event does not exist").build();
        }
        eventDAO.deleteEvent(event);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
