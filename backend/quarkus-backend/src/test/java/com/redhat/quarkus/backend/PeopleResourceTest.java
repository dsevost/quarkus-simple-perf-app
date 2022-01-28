package com.redhat.quarkus.backend;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import java.time.LocalDate;

import javax.ws.rs.core.Response;

@QuarkusTest
public class PeopleResourceTest {

    private static final String PEOPLE_PATH = "/people";

    @Test
    @Order(10)
    public void tesFindAll() {
        given()
                .accept("application/json")
                .when().get(PEOPLE_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(
                        containsString("Alice"),
                        containsString("Bob"),
                        containsString("Tyler"));
    }

    @Test
    @Order(20)
    public void testFindById() {
        given()
                .accept("application/json")
                .when().get(PEOPLE_PATH + "/2")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsString("Bob"));
    }

    @Test
    @Order(30)
    public void testCreate1() {
        final String NAME = "Just created1";
        Person p = Person.by(NAME, LocalDate.now());
        given()
                .accept("application/json")
                .contentType("application/json")
                .when().body(p).post(PEOPLE_PATH)
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(31)
    public void testCreate2() {
        final String NAME = "Just created2";
        Person p = Person.by(NAME, LocalDate.now());
        given()
                .accept("application/json")
                .contentType("application/json")
                .when().queryParam("returnBlank", "false").body(p).post(PEOPLE_PATH)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsString(NAME));
    }

    @Test
    @Order(40)
    public void testUpdateById() {
        Person p = Person.by("Bobby", LocalDate.of(1999, 12, 31));
        given()
                .accept("application/json")
                .contentType("application/json")
                .when().body(p).put(PEOPLE_PATH + "/2")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(
                        containsString("Bobby"),
                        containsString("1999-12-31"));
    }

    @Test
    @Order(50)
    public void testDeleteById() {
        given()
                .accept("application/json")
                .when().delete(PEOPLE_PATH + "/1")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        given()
                .accept("application/json")
                .when().get(PEOPLE_PATH + "/1")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}
