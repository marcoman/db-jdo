/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

/*
 *  PersistentClean.java    March 10, 2001
 */

package org.apache.jdo.impl.state;

import javax.jdo.Transaction;

/**
 * This class represents PersistentClean state specific state transitions as 
 * requested by StateManagerImpl. This state is the result of any of the 
 * following operations:
 *     - read field in an active datastore transaction;
 *     - makeTransactional call on a PersistentNonTransactional instance;
 *     - refresh of a PersistentDirty instance in an active datastore transaction.
 *
 * @author Marina Vatkina
 */
class PersistentClean extends LifeCycleState {

    PersistentClean() {
        // these flags are set only in the constructor 
        // and shouldn't be changed afterwards
        // (cannot make them final since they are declared in superclass 
        // but their values are specific to subclasses)
        isPersistent = true;
        isTransactional = true;
        isDirty = false;
        isNew = false;
        isDeleted = false;

        isFlushed = true;
        isNavigable = true;
        isRefreshable = true;
        isBeforeImageUpdatable = false;

        stateType = P_CLEAN;
    }

   /**
    * @see LifeCycleState#transitionDeletePersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionDeletePersistent(StateManagerImpl sm) {    
        sm.registerTransactional();
        sm.preDelete();
        return changeState(P_DELETED);
    }

   /**
    * @see LifeCycleState#transitionMakeNontransactional(StateManagerImpl sm,
    *  Transaction tx)
    */
    protected LifeCycleState transitionMakeNontransactional(StateManagerImpl sm,
        Transaction tx) {
        sm.registerNonTransactional();
        return changeState(P_NON_TX);
    }

   /**
    * @see LifeCycleState#transitionMakeTransient(StateManagerImpl sm, 
    * Transaction tx)
    */
    protected LifeCycleState transitionMakeTransient(StateManagerImpl sm, 
        Transaction tx) {
        sm.disconnect();
        return changeState(TRANSIENT);
    }
    
   /**
    * @see LifeCycleState#transitionEvict(StateManagerImpl sm, Transaction tx)
    */
    protected LifeCycleState transitionEvict(StateManagerImpl sm, Transaction tx) {
        sm.clearFields();
        sm.reset();
        sm.registerNonTransactional();
        return changeState(HOLLOW);
    }

   /**
    * @see LifeCycleState#transitionRetrieve(StateManagerImpl sm, Transaction tx)
    */
    protected LifeCycleState transitionRetrieve(StateManagerImpl sm,
        Transaction tx) {

        sm.loadUnloaded();
        return this;
    }

   /**
    * @see LifeCycleState#transitionWriteField(StateManagerImpl sm, Transaction tx)
    */
    protected LifeCycleState transitionWriteField(StateManagerImpl sm, 
        Transaction tx) {

        int newState;
        boolean transactionActive = tx.isActive();

        if (transactionActive) {
            // This is the first write request: prepare BeforeImage
            sm.createBeforeImage();
            sm.registerTransactional();
            newState = P_DIRTY;

        } else {
            if (!tx.getNontransactionalWrite()) {
                assertTransaction(false);
            }
            sm.registerNonTransactional();
            newState = P_NON_TX;
        }
        return changeState(newState);
    }

   /** 
    * @see LifeCycleState#transitionCommit(boolean retainValues, StateManagerImpl sm)
    */
    protected LifeCycleState transitionCommit(boolean retainValues, StateManagerImpl sm) {
        int newstate;

        if (retainValues) {
            sm.replaceSCOFields();
            newstate = P_NON_TX;
        } else {
            sm.clearFields();
            newstate = HOLLOW;
        }

        sm.reset();
        return changeState(newstate);
    }
}

