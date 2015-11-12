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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import java.net.URI;
import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PhonebookServiceHanderTest {

    @Mock
    EntityManager em;
    @Mock
    UserTransaction utx;
    @Mock
    CriteriaBuilder builder;
    @Mock
    CriteriaQuery<PhonebookEntry> criteriaQuery;
    @Mock
    Root<PhonebookEntry> root;
    @Mock
    TypedQuery<PhonebookEntry> query;
    @Mock
    UriInfo uriInfo;
    @Mock
    UriBuilder uriBuilder;

    @Test
    public void queryPhonebook() {

        when(em.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createQuery(PhonebookEntry.class)).thenReturn(criteriaQuery);
        when(em.createQuery(criteriaQuery)).thenReturn(query);
        when(em.createQuery("SELECT t FROM PhonebookEntry t", PhonebookEntry.class)).thenReturn(query);
        when(criteriaQuery.select(Matchers.any(root.getClass()))).thenReturn(criteriaQuery);
        when(query.getResultList())
                .thenReturn(Arrays.asList(new PhonebookEntry("Mr", "John", "Doe", "12345")));

        final PhonebookServiceHandler phonebookServiceHandler = new PhonebookServiceHandler(utx, em, uriInfo);
        final PhonebookEntries entries = phonebookServiceHandler.queryPhonebook(null, null, null);

        assertEquals(1, entries.getEntries().size());
        assertEquals("John", entries.getEntries().get(0).getFirstName());

    }

    @Test
    public void getEntry() {
        final long id = 1;
        when(em.find(PhonebookEntry.class, id)).thenReturn(new PhonebookEntry("Mr", "John", "Doe", "12345"));

        final PhonebookServiceHandler phonebookServiceHandler = new PhonebookServiceHandler(utx, em, uriInfo);
        final PhonebookEntry entry = phonebookServiceHandler.getEntry("1");

        assertEquals("John", entry.getFirstName());
    }

    @Test(expected = NotFoundException.class)
    public void getEntryNotFound() {
        final long id = 1;
        when(em.find(PhonebookEntry.class, id)).thenReturn(null);

        final PhonebookServiceHandler phonebookServiceHandler = new PhonebookServiceHandler(utx, em, uriInfo);
        final PhonebookEntry entry = phonebookServiceHandler.getEntry("1");

    }

    @Test
    public void create() {
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.scheme(Matchers.anyString())).thenReturn(uriBuilder);
        when(uriBuilder.host(Matchers.anyString())).thenReturn(uriBuilder);
        when(uriBuilder.path(Matchers.anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(URI.create("http://localhost:1234/api/phonebook"));

        final long id = 1;
        final PhonebookServiceHandler phonebookServiceHandler = new PhonebookServiceHandler(utx, em, uriInfo);
        final PhonebookEntry entry = new PhonebookEntry("Mr", "John", "Doe", "12345");
        entry.setId(id);
        final Response response = phonebookServiceHandler.create(entry);

        assertEquals(201, response.getStatus());
    }

    @Test
    public void update() {
        final long id = 1;
        when(em.find(PhonebookEntry.class, id)).thenReturn(new PhonebookEntry("Mr", "John", "Doe", "12345"));

        final PhonebookServiceHandler phonebookServiceHandler = new PhonebookServiceHandler(utx, em, uriInfo);
        final PhonebookEntry entry = new PhonebookEntry("Mr", "Jack", "Doe", "12345");
        final Response response = phonebookServiceHandler.update("1", entry);

        assertEquals(204, response.getStatus());
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        final long id = 1;
        when(em.find(PhonebookEntry.class, id)).thenReturn(null);

        final PhonebookServiceHandler phonebookServiceHandler = new PhonebookServiceHandler(utx, em, uriInfo);
        final PhonebookEntry entry = new PhonebookEntry("Mr", "Jack", "Doe", "12345");
        phonebookServiceHandler.update("1", entry);
    }

    @Test
    public void deleteEntry() {
        final long id = 1;
        when(em.find(PhonebookEntry.class, id)).thenReturn(new PhonebookEntry("Mr", "John", "Doe", "12345"));

        final PhonebookServiceHandler phonebookServiceHandler = new PhonebookServiceHandler(utx, em, uriInfo);
        final Response response = phonebookServiceHandler.deleteEntry("1");

        assertEquals(204, response.getStatus());
    }

    @Test(expected = NotFoundException.class)
    public void deleteEntryNotFound() {
        final long id = 1;
        when(em.find(PhonebookEntry.class, id)).thenReturn(null);

        final PhonebookServiceHandler phonebookServiceHandler = new PhonebookServiceHandler(utx, em, uriInfo);
        phonebookServiceHandler.deleteEntry("1");

    }

}
