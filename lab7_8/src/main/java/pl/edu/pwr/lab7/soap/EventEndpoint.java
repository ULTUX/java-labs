package pl.edu.pwr.lab7.soap;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pwr.lab7.jpa.event.Event;
import pl.edu.pwr.lab7.jpa.event.EventService;
import pl.edu.pwr.lab7.soap.event.*;

@Transactional
@Endpoint
public class EventEndpoint {

    private final EventService eventService;

    public EventEndpoint(EventService eventService) {
        this.eventService = eventService;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getAllEventRequest")
    @ResponsePayload
    public GetAllEventResponse getAll(@RequestPayload GetAllEventRequest req) {
        var resp = new GetAllEventResponse();
        resp.getEventList().addAll(eventService.getAll());
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "getEventRequest")
    @ResponsePayload
    public GetEventResponse get(@RequestPayload GetEventRequest req) {
        var event = eventService.getById(req.getId());
        event = (Event) Hibernate.unproxy(event);
        var resp = new GetEventResponse();
        resp.setEvent(event);
        return resp;
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "addEventRequest")
    @ResponsePayload
    public AddEventResponse add(@RequestPayload AddEventRequest req) {
        var event = new Event();
        event.setLocation(req.getLocation());
        event.setTime(req.getTime());
        event.setName(req.getName());
        eventService.addEvent(event);
        return new AddEventResponse();
    }

    @PayloadRoot(namespace = "http://pwr.edu.pl/soap", localPart = "deleteEventRequest")
    @ResponsePayload
    public DeleteEventResponse delete(@RequestPayload DeleteEventRequest req) {
        eventService.deleteById(req.getId());
        return new DeleteEventResponse();
    }

}
