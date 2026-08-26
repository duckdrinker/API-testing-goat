package com.example.fixtures;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.annotation.security.RolesAllowed;

import java.util.Arrays;
import java.util.List;

/**
 * Second, simpler JAX-RS resource. Deliberately uses the {@code jakarta.ws.rs.*}
 * namespace (instead of {@code javax.ws.rs.*}, used by {@link UserResource}) to
 * confirm the detector's import gate accepts either package for the same
 * "jaxrs" framework, purely from the JAX-RS annotations, without needing
 * anything specific to a particular application server or runtime.
 *
 * See test-plan-apisec-framework-discovery.html.
 */
@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    // GET /api/books
    @GET
    @RolesAllowed({"USER", "ADMIN"})
    public List<String> listBooks() {
        return Arrays.asList("Java Concurrency in Practice", "Effective Java");
    }

    // GET /api/books/{id}
    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    public String getBook(@PathParam("id") String id) {
        return "Book " + id;
    }

    // POST /api/books
    // - Unannotated, non-primitive parameter -> implicit BODY, name="body".
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public String createBook(BookPayload payload) {
        return "created:" + payload.title;
    }

    /** Plain POJO used as the implicit-body payload for createBook(). */
    public static class BookPayload {
        public String title;
        public String author;
    }
}
