package com.gbs.ibm.system;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.gbs.ibm.system.Event;


import javax.enterprise.context.RequestScoped;

@RequestScoped
public class EventDAO {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public void createEvent(Event event) {
        em.persist(event);
    }

    public Event readEvent(int eventId) {
        return em.find(Event.class, eventId);
    }

    public void updateEvent(Event event) {
        em.merge(event);
    }

    public void deleteEvent(Event event) {
        em.remove(event);
    }

    public List<Event> readAllEvents() {
        return em.createNamedQuery("Event.findAll", Event.class).getResultList();
    }

    public List<Event> findEvent(String name, String location, String time) {
        return em.createNamedQuery("Event.findEvent", Event.class)
            .setParameter("name", name)
            .setParameter("location", location)
            .setParameter("time", time).getResultList();
    }
}
