/**
 * Copyright 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibmcloud.contest.phonebook;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;

@Path("/phonebook")
@Api(value = "/phonebook", authorizations = { @Authorization(value = "apikey_auth") })
/**
 * CRUD service for phonebook table. It uses REST Style
 *
 */
public class PhonebookServiceHandler implements ReaderListener {
    @Context
    UriInfo uriInfo;

    private final UserTransaction utx;
    private final EntityManager em;

    public PhonebookServiceHandler() {
        utx = getUserTransaction();
        em = getEm();
    }

    public PhonebookServiceHandler(final UserTransaction utx, final EntityManager em, final UriInfo uriInfo) {
        this.utx = utx;
        this.em = em;
        this.uriInfo = uriInfo;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns list of entries matching the query")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = PhonebookEntries.class),
            @ApiResponse(code = 401, message = "User not authorized") })
    public PhonebookEntries queryPhonebook(
            @ApiParam(hidden = true) @QueryParam("Authorization") final String userkey,
            @QueryParam("title") final String title, @QueryParam("firstname") final String firstname,
            @QueryParam("lastname") final String lastname, @QueryParam("email") final String email) {

        if (!authenticateUser(userkey)) {
            throw new UnauthorizedException();
        }
        final List<PhonebookEntry> checkList = em
                .createQuery("SELECT t FROM PhonebookEntry t WHERE t.userkey = :user", //$NON-NLS-1$
                        PhonebookEntry.class)
                .setParameter("user", userkey).getResultList(); //$NON-NLS-1$
        if (checkList.size() == 0) {
            createSampleData(userkey);
        }

        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        final CriteriaQuery<PhonebookEntry> criteriaQuery = criteriaBuilder.createQuery(PhonebookEntry.class);
        final Root<PhonebookEntry> entry = criteriaQuery.from(PhonebookEntry.class);
        final List<Predicate> predicates = new ArrayList<Predicate>();

        predicates.add(criteriaBuilder.equal(entry.get("userkey"), userkey)); //$NON-NLS-1$
        if (title != null) {
            predicates.add(criteriaBuilder.equal(entry.get("title"), title)); //$NON-NLS-1$
        }
        if (firstname != null) {
            predicates.add(criteriaBuilder.equal(entry.get("firstName"), firstname)); //$NON-NLS-1$
        }
        if (lastname != null) {
            predicates.add(criteriaBuilder.equal(entry.get("lastName"), lastname)); //$NON-NLS-1$
        }
        if (email != null) {
            predicates.add(criteriaBuilder.equal(entry.get("email"), email)); //$NON-NLS-1$
        }

        criteriaQuery.select(entry).where(predicates.toArray(new Predicate[] {}));
        final List<PhonebookEntry> entryList = em.createQuery(criteriaQuery).getResultList();

        final PhonebookEntries entries = new PhonebookEntries();
        entries.setEntries(entryList);
        return entries;

    }

    @GET
    @Path("favorites")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns list of favorite entries")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = PhonebookEntries.class),
            @ApiResponse(code = 401, message = "User not authorized") })
    public PhonebookEntries getFavorites(
            @ApiParam(hidden = true) @QueryParam("Authorization") final String userkey) {

        if (!authenticateUser(userkey)) {
            throw new UnauthorizedException();
        }
        final List<PhonebookEntry> entryList = em
                .createQuery("SELECT t FROM PhonebookEntry t WHERE t.favorite = 1 AND t.userkey = :user", //$NON-NLS-1$
                        PhonebookEntry.class)
                .setParameter("user", userkey) //$NON-NLS-1$
                .getResultList();

        final PhonebookEntries entries = new PhonebookEntries();
        entries.setEntries(entryList);
        return entries;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns entry with provided ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = PhonebookEntries.class),
            @ApiResponse(code = 401, message = "User not authorized"),
            @ApiResponse(code = 404, message = "Entry not found for given ID") })
    public PhonebookEntry getEntry(@ApiParam(hidden = true) @QueryParam("Authorization") final String userkey,
            @PathParam("id") final String id) {

        if (!authenticateUser(userkey)) {
            throw new UnauthorizedException();
        }

        final Long queryId = Long.parseLong(id);
        final List<PhonebookEntry> dbEntries = em
                .createQuery("SELECT t FROM PhonebookEntry t WHERE t.id = :id AND t.userkey = :user", //$NON-NLS-1$
                        PhonebookEntry.class)
                .setParameter("id", queryId).setParameter("user", userkey).setMaxResults(1) //$NON-NLS-1$//$NON-NLS-2$
                .getResultList();
        if (dbEntries.size() == 1) {
            return dbEntries.get(0);
        } else {
            throw new NotFoundException();
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Adds entry to phonebook")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Created successfully"),
            @ApiResponse(code = 401, message = "User not authorized") })
    public Response create(@ApiParam(hidden = true) @QueryParam("Authorization") final String userkey,
            final PhonebookEntry entry) {

        if (!authenticateUser(userkey)) {
            throw new UnauthorizedException();
        }

        entry.setUserKey(userkey);

        try {
            utx.begin();
            em.persist(entry);
            utx.commit();
            final URI uri = new URI(uriInfo.getAbsolutePath() + "/" + String.valueOf(entry.getId())); //$NON-NLS-1$
            return Response.created(uri).build();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new WebApplicationException();
        } finally {
            try {
                if (utx.getStatus() == Status.STATUS_ACTIVE) {
                    utx.rollback();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

    }

    @POST
    @Path("favorites/{id}")
    @ApiOperation(value = "Sets the favorite status of an entry in the phonebook")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Favorite set successfully"),
            @ApiResponse(code = 400, message = "Bad arguments"),
            @ApiResponse(code = 401, message = "User not authorized"),
            @ApiResponse(code = 404, message = "Entry not found for given ID") })
    public Response setFavorite(@ApiParam(hidden = true) @QueryParam("Authorization") final String userkey,
            @PathParam("id") final String id, @QueryParam("setting") final String setting) {

        if (!authenticateUser(userkey)) {
            throw new UnauthorizedException();
        }

        if (setting == null || !setting.equals("true") && !setting.equals("false")) { //$NON-NLS-1$//$NON-NLS-2$
            throw new BadRequestException();
        }
        final Long queryId = Long.parseLong(id);
        final List<PhonebookEntry> dbEntries = em
                .createQuery("SELECT t FROM PhonebookEntry t WHERE t.id = :id AND t.userkey = :user", //$NON-NLS-1$
                        PhonebookEntry.class)
                .setParameter("id", queryId).setParameter("user", userkey).setMaxResults(1) //$NON-NLS-1$//$NON-NLS-2$
                .getResultList();
        if (dbEntries.size() != 1) {
            throw new NotFoundException();
        }
        final PhonebookEntry dbEntry = dbEntries.get(0);
        final Boolean favorite = Boolean.parseBoolean(setting);
        try {
            utx.begin();
            dbEntry.setFavorite(favorite);
            em.merge(dbEntry);
            utx.commit();
            final URI uri = new URI(uriInfo.getAbsolutePath().toString());
            return Response.created(uri).build();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new WebApplicationException();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Updates an existing entry in the phonebook")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 401, message = "User not authorized"),
            @ApiResponse(code = 404, message = "Entry not found for given ID") })
    public Response update(@ApiParam(hidden = true) @QueryParam("Authorization") final String userkey,
            @PathParam("id") final String id, final PhonebookEntry entry) {

        if (!authenticateUser(userkey)) {
            throw new UnauthorizedException();
        }

        final Long queryId = Long.parseLong(id);

        final List<PhonebookEntry> dbEntries = em
                .createQuery("SELECT t FROM PhonebookEntry t WHERE t.id = :id AND t.userkey = :user", //$NON-NLS-1$
                        PhonebookEntry.class)
                .setParameter("id", queryId).setParameter("user", userkey).setMaxResults(1) //$NON-NLS-1$//$NON-NLS-2$
                .getResultList();
        if (dbEntries.size() != 1) {
            throw new NotFoundException();
        }
        final PhonebookEntry dbEntry = dbEntries.get(0);
        try {
            utx.begin();
            dbEntry.setTitle(entry.getTitle());
            dbEntry.setFirstName(entry.getFirstName());
            dbEntry.setLastName(entry.getLastName());
            dbEntry.setPhoneNumber(entry.getPhoneNumber());
            dbEntry.setEmail(entry.getEmail());
            em.merge(dbEntry);
            utx.commit();
            return Response.noContent().build();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new WebApplicationException();
        }

    }

    @DELETE
    @Path("{id}")
    @ApiOperation(value = "Deletes an existing entry from the phonebook")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 401, message = "User not authorized"),
            @ApiResponse(code = 404, message = "Entry not found for given ID") })
    public Response deleteEntry(@ApiParam(hidden = true) @QueryParam("Authorization") final String userkey,
            @PathParam("id") final String id) {

        if (!authenticateUser(userkey)) {
            throw new UnauthorizedException();
        }

        final Long queryId = Long.parseLong(id);

        final List<PhonebookEntry> dbEntries = em
                .createQuery("SELECT t FROM PhonebookEntry t WHERE t.id = :id AND t.userkey = :user", //$NON-NLS-1$
                        PhonebookEntry.class)
                .setParameter("id", queryId).setParameter("user", userkey).setMaxResults(1) //$NON-NLS-1$//$NON-NLS-2$
                .getResultList();
        if (dbEntries.size() != 1) {
            throw new NotFoundException();
        }
        final PhonebookEntry dbEntry = dbEntries.get(0);
        try {
            utx.begin();
            em.remove(em.merge(dbEntry));
            utx.commit();
            return Response.noContent().build();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new WebApplicationException();
        }

    }

    private void createSampleData(final String userkey) {
        create(userkey, new PhonebookEntry("Mr", "Fred", "Jones", "01962 000000", "fjones@email.com")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        create(userkey, new PhonebookEntry("Mrs", "Jane", "Doe", "01962 000001", "jdoe@email.com")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    }

    private UserTransaction getUserTransaction() {
        InitialContext ic;
        try {
            ic = new InitialContext();
            return (UserTransaction) ic.lookup("java:comp/UserTransaction"); //$NON-NLS-1$
        } catch (final NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private EntityManager getEm() {
        InitialContext ic;
        try {
            ic = new InitialContext();
            return (EntityManager) ic.lookup("java:comp/env/openjpa-phonebook/entitymanager"); //$NON-NLS-1$
        } catch (final NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean authenticateUser(final String userkey) {
        if (userkey == null) {
            return false;
        }
        final UserEntry checkEntry = em.find(UserEntry.class, userkey);
        return (checkEntry != null);
    }

    /** {@inheritDoc} */
    @Override
    public void afterScan(final Reader reader, final Swagger swagger) {

        final ApiKeyAuthDefinition authScheme = new ApiKeyAuthDefinition();
        authScheme.setIn(In.QUERY);
        authScheme.setName("Authorization"); //$NON-NLS-1$

        swagger.addSecurityDefinition("apikey_auth", authScheme); //$NON-NLS-1$

    }

    /** {@inheritDoc} */
    @Override
    public void beforeScan(final Reader arg0, final Swagger arg1) {
        // TODO Auto-generated method stub

    }

}
