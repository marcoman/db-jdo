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

package org.apache.jdo.model.jdo;

import org.apache.jdo.model.ModelException;
import org.apache.jdo.model.java.JavaType;


/**
 * A JDOCollection instance represents the JDO relationship metadata 
 * of a collection relationship field. 
 *
 * @author Michael Bouschen
 */
public interface JDOCollection
    extends JDORelationship 
{
    /**
     * Determines whether the values of the elements should be stored if 
     * possible as part of the instance instead of as their own instances 
     * in the datastore.
     * @return <code>true</code> if the elements should be stored as part of 
     * the instance; <code>false</code> otherwise
     */
    public boolean isEmbeddedElement();
    
    /**
     * Set whether the values of the elements should be stored if possible as 
     * part of the instance instead of as their own instances in the datastore.
     * @param embeddedElement <code>true</code> if elements should be stored 
     * as part of the instance
     * @exception ModelException if impossible
     */
    public void setEmbeddedElement(boolean embeddedElement)
        throws ModelException;

    /** 
     * Get the type representation of the collection elements. 
     * @return the element type
     */
    public JavaType getElementType();

    /** 
     * Set the type representation of the collection elements.
     * @param elementType the type representation of the collection elements
     * @exception ModelException if impossible
     */
    public void setElementType(JavaType elementType)
        throws ModelException;

    /** 
     * Get the type of collection elements as string.
     * @return the element type as string
     */
    public String getElementTypeName();

    /** 
     * Set string representation of the type of collection elements.
     * @param elementTypeName a string representation of the type of elements in
     * the collection. 
     * @exception ModelException if impossible
     */
    public void setElementTypeName(String elementTypeName)
        throws ModelException;

}
