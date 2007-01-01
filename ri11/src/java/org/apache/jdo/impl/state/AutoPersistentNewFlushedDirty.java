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
 *  AutoPersistentNewFlushedDirty.java    September 4, 2001
 */

package org.apache.jdo.impl.state;

import java.util.BitSet;

import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.store.StoreManager;


/**
 * This class represents AutoPersistentNewFlushedDirty state specific state
 * transitions as requested by StateManagerImpl. This state is the result
 * calls: makePersistent-flush-write_field on the same instance.
 *
 * @author Marina Vatkina
 */
class AutoPersistentNewFlushedDirty extends PersistentNewFlushedDirty {
    
    protected AutoPersistentNewFlushedDirty() {
        super();

        isAutoPersistent = true;
        stateType = AP_NEW_FLUSHED_DIRTY;
    }

   /**
    * @see LifeCycleState#transitionMakePersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionMakePersistent(StateManagerImpl sm) {
        return changeState(P_NEW_FLUSHED_DIRTY);
    }

   /**
    * @see LifeCycleState#transitionDeletePersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionDeletePersistent(StateManagerImpl sm) {
        sm.preDelete();
        return changeState(P_NEW_FLUSHED_DELETED);
    }

   /**
    * @see LifeCycleState#flush(BitSet loadedFields, BitSet dirtyFields,
    *   StoreManager srm, StateManagerImpl sm)
    */
    protected LifeCycleState flush(BitSet loadedFields, BitSet dirtyFields,
        StoreManager srm, StateManagerImpl sm) {
        if (sm.getPersistenceManager().insideCommit()) {
            // Need to restore state - it is unreachable at commit:
            if (srm.delete(loadedFields, dirtyFields, sm) ==
                StateManagerInternal.FLUSHED_COMPLETE) {
                sm.markAsFlushed();
                return changeState(AP_PENDING); 
            }

        } else { // flush for query
            // Need to update state - it is flush for query
            if (srm.update(loadedFields, dirtyFields, sm) ==
                StateManagerInternal.FLUSHED_COMPLETE) {

                // Do NOT mark as flushed - it needs to be processed again at commit.
                return changeState(AP_NEW_FLUSHED);
            }
        }
        return this;
    }

   /**
    * Differs from the generic implementation in LifeCycleState that state transitions
    * to Transient and restores all fields. Called only if state becomes not reachable.
    * @see LifeCycleState#transitionRollback(boolean restoreValues, StateManagerImpl sm)
    */
    protected LifeCycleState transitionRollback(boolean restoreValues, StateManagerImpl sm) {
        if (restoreValues) {
            sm.restoreFields();
        } else {
            sm.unsetSCOFields();
        }
        sm.disconnect();
        return changeState(TRANSIENT);
    }
}

