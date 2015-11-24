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
package com.ibmcloud.contest.phonebook.util;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class CustomUserTransaction implements UserTransaction {

    private final EntityManager em;

    public CustomUserTransaction(final EntityManager em) {
        this.em = em;
    }

    /** {@inheritDoc} */
    @Override
    public void begin() throws NotSupportedException, SystemException {
        em.getTransaction().begin();
    }

    /** {@inheritDoc} */
    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException,
            SecurityException, IllegalStateException, SystemException {
        em.getTransaction().commit();
    }

    /** {@inheritDoc} */
    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        em.getTransaction().rollback();

    }

    /** {@inheritDoc} */
    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        em.getTransaction().setRollbackOnly();
    }

    /** {@inheritDoc} */
    @Override
    public int getStatus() throws SystemException {
        if (em.getTransaction().isActive()) {
            return Status.STATUS_ACTIVE;
        } else {
            return Status.STATUS_NO_TRANSACTION;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setTransactionTimeout(final int seconds) throws SystemException {
        throw new SystemException("setTransactionTimeout() Not supported"); //$NON-NLS-1$
    }
}