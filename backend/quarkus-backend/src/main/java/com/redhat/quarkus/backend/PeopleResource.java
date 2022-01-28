package com.redhat.quarkus.backend;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import io.quarkus.panache.common.Sort;

@Path("/people")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PeopleResource {

    private static final Logger LOGGER = Logger.getLogger(PeopleResource.class);

    @PostConstruct
    void init() {
        LOGGER.infof("Working directory: %s", new File("").getAbsolutePath());
    }

    @Inject
    PersonRepository repo;

    private static Response response(Person p) {
        return response(p, null);
    }

    private static Response response(Response.Status s) {
        return response(null, s);
    }

    private static Response response(Person p, Response.Status s) {
        if (p == null) {
            return s == null ? Response.ok().build() : Response.status(s).build();
        } else {
            return s == null ? Response.ok(p).build() : Response.ok(p).status(s).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response find(@PathParam("id") Long id) {
        LOGGER.debugf("Looking Person for id: %s", id);
        Person person = repo.findById(id);
        if (person == null) {
            LOGGER.debugf("Person for id: %s not found", id);
            return response(Response.Status.NOT_FOUND);
        } else {

        }
        LOGGER.debugf("Found Person(%s) for id: %s", person, id);
        return response(person);
    }

    @GET
    @Produces("application/json")
    public Response findAll(@QueryParam("sort") @DefaultValue("name") String sortQuery) {
        LOGGER.debugf("Looking for all people, sort by: %s", sortQuery);
        List<Person> people = repo.listAll(Sort.by(sortQuery));
        LOGGER.debugf("Foud %s people", people.size());
        return Response.ok(people).build();
    }

    @Transactional
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(@QueryParam("returnBlank") @DefaultValue("true") Boolean returnBlank, Person person) {
        LOGGER.debugf("Person for create: %s", person);
        if (person.getId() != null) {
            final String err = String.format("Person object is invalid, Person.ID is set: %s", person.getId());
            LOGGER.debugf("Error while create: %s", err);
            return response(Response.Status.EXPECTATION_FAILED);
        }

        repo.persist(person);
        LOGGER.infof("Created Person: %s", person);
        if (returnBlank == true) {
            return response(Response.Status.CREATED);
        } else {
            return response(person);
        }
    }

    @Transactional
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") Long id, Person person) {
        LOGGER.debugf("Person for update: id=%s, person=%s", id, person);
        if (person.getName() == null) {
            final String err = "Person Name was not set on request";
            LOGGER.debugf("Error while update: %s", err);
            return response(Response.Status.EXPECTATION_FAILED);
        }

        if (person.getBirth() == null) {
            final String err = "Person Birth was not set on request";
            LOGGER.debugf("Error while update: %s", err);
            return response(Response.Status.EXPECTATION_FAILED);
        }

        Person entity = repo.findById(id);
        if (entity == null) {
            final String err = String.format("Person with id '%s' not found, update failed", id);
            LOGGER.debugf("Error while update: %s", err);
            return response(Response.Status.NOT_FOUND);
        }

        entity.setName(person.getName());
        entity.setBirth(person.getBirth());
        repo.persist(entity);
        LOGGER.debugf("Updated person: %s", entity);
        return response(entity);
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        LOGGER.debugf("Delete Person for id: %s", id);
        Person entity = repo.findById(id);
        if (entity == null) {
            final String err = String.format("Person with id '%s' not found, update failed", id);
            LOGGER.debugf("Error while update: %s", err);
            return response(Response.Status.NOT_FOUND);
        }
        repo.delete(entity);
        LOGGER.debugf("Person %s removed", entity);
        return response(Response.Status.NO_CONTENT);
    }
}
