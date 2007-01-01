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

package org.apache.jdo.impl.model.java;

import org.apache.jdo.model.java.JavaType;

/**
 * A JDOSupportedMapType instance represents a JDO supported map type. 
 * <p>
 * Class PredefinedType provides public static final variables referring
 * to the JavaType representation for JDO supported map types.
 *
 * @see PredefinedType#mapType
 * @see PredefinedType#hashMapType
 * @see PredefinedType#hashtableType
 * @see PredefinedType#propertiesType
 * @see PredefinedType#treeMapType
 *
 * @author Michael Bouschen
 * @since JDO 1.0.1
 */
public class JDOSupportedMapType
    extends PredefinedType
{
    /** 
     * Constructor for JDOSupportedMap types having no superclass. This is
     * the map interface among the JDO supported map types.
     * @param clazz the Class instance representing the type
     */
    public JDOSupportedMapType(Class clazz)
    {
        super(clazz);
    }

    /** 
     * Constructor for JDOSupportedMap types having a superclass. These are
     * the map implemenatation classes among the JDO supported map types.
     * @param clazz the Class instance representing the type
     * @param superclass JavaType instance representing the superclass.
     */
    public JDOSupportedMapType(Class clazz, JavaType superclass)
    {
        super(clazz, superclass);
    }

    /** 
     * Returns <code>true</code> if this JavaType represents a JDO
     * supported map type. The JDO specification allows the
     * following map interfaces and classes as types of persistent 
     * fields (see section 6.4.3 Persistent fields):
     * @return <code>true</code> if this JavaTypre represents a JDO
     * supported map; <code>false</code> otherwise.
     */
    public boolean isJDOSupportedMap() 
    {
        return true;
    }

    /**
     * Returns <code>true</code> if this JavaType represents a trackable
     * Java class. A JDO implementation may replace a persistent field of
     * a trackable type with an assignment compatible instance of its own
     * implementation of this type which notifies the owning FCO of any
     * change of this field. 
     * @return <code>true</code> if this JavaType represents a trackable
     * Java class, <code>false</code> otherwise.
     */
    public boolean isTrackable()
    {
        return true;
    }
    
}
