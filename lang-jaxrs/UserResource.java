package com.example.fixtures;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

/**
 * Fixture resource for the JAX-RS API Discovery framework detector.
 *
 * See test-plan-apisec-framework-discovery.html.
 *
 * Covers all seven HTTP verbs and every parameter-annotation mapping the
 * detector recognizes, spread across distinct methods so each combination
 * can be asserted independently.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    // GET /api/users/{id}
    // - @PathParam("id")            -> PATH
    // - @QueryParam("expand") with @DefaultValue("false") -> QUERY, optional
    // - @HeaderParam("X-Request-Id") -> HEADER
    // - @Context SecurityContext    -> discarded as a parameter, but generates a
    //   "programmatic" security requirement.
    // - @RolesAllowed({"USER", "ADMIN"})
    @GET
    @Path("/{id}")
    @RolesAllowed({"USER", "ADMIN"})
    public String getUser(
            @PathParam("id") String id,
            @QueryParam("expand") @DefaultValue("false") String expand,
            @HeaderParam("X-Request-Id") String requestId,
            @Context SecurityContext securityContext) {
        return "user:" + id;
    }

    // GET /api/users/search
    // - @Encoded on the method -> tag.
    // - @QueryParam("q")             -> QUERY
    // - @MatrixParam("sort")         -> QUERY (deliberate mapping, not a MATRIX location)
    // - @Context UriInfo             -> discarded entirely, must NOT appear as a parameter.
    // - @PermitAll
    @GET
    @Path("/search")
    @Encoded
    @PermitAll
    public String searchUsers(
            @QueryParam("q") String query,
            @MatrixParam("sort") String sort,
            @Context UriInfo uriInfo) {
        return "results-for:" + query;
    }

    // POST /api/users
    // - Unannotated, non-primitive parameter -> implicit BODY, name="body".
    // - Method-level @Consumes/@Produces override the class-level ones.
    // - @RolesAllowed({"ADMIN"})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public String createUser(UserPayload payload) {
        return "created:" + payload.email;
    }

    // PUT /api/users/{id}
    // - @PathParam("id")   -> PATH
    // - @BeanParam          -> BODY
    // - @DenyAll
    @PUT
    @Path("/{id}")
    @DenyAll
    public String updateUser(
            @PathParam("id") String id,
            @BeanParam UserUpdateBean bean) {
        return "updated:" + id;
    }

    // DELETE /api/users/{id}
    // - @PathParam("id")        -> PATH
    // - @CookieParam("session") -> COOKIE
    // - @Deprecated marks the endpoint as deprecated.
    // - @RolesAllowed({"ADMIN"})
    @DELETE
    @Path("/{id}")
    @Deprecated
    @RolesAllowed({"ADMIN"})
    public void deleteUser(
            @PathParam("id") String id,
            @CookieParam("session") String session) {
        // no-op fixture body
    }

    // PATCH /api/users/{id}/profile
    // - @PathParam("id")           -> PATH
    // - @FormParam("nickname")     -> BODY
    // - Method-level @Consumes for a form submission.
    @PATCH
    @Path("/{id}/profile")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String patchUserProfile(
            @PathParam("id") String id,
            @FormParam("nickname") String nickname) {
        return "patched:" + id;
    }

    // HEAD /api/users/{id}
    // - @PathParam("id") -> PATH
    @HEAD
    @Path("/{id}")
    public void headUser(@PathParam("id") String id) {
        // no-op fixture body
    }

    // OPTIONS /api/users
    @OPTIONS
    public String optionsUsers() {
        return "GET,POST,PUT,DELETE,PATCH,HEAD,OPTIONS";
    }

    /** Plain POJO used as the implicit-body payload for createUser(). */
    public static class UserPayload {
        public String name;
        public String email;
    }

    /** Bean-param carrier used by updateUser(); fields mirror typical @BeanParam usage. */
    public static class UserUpdateBean {
        @QueryParam("notify")
        public boolean notify;

        @FormParam("nickname")
        public String nickname;
    }
}
