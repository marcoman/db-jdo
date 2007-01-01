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

/**
 * TypeSupport.java
 *
 */

package org.apache.jdo.impl.model.jdo.util;

import java.util.Set;
import java.util.HashSet;

import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOModel;


/**
 *
 */
public class TypeSupport
{
    /** */
    private static Set primitiveTypeNames = new HashSet();

    static 
    {
        // initialize set of names of primitive types
        primitiveTypeNames.add("byte");
        primitiveTypeNames.add("short");
        primitiveTypeNames.add("int");
        primitiveTypeNames.add("long");
        primitiveTypeNames.add("char");
        primitiveTypeNames.add("float");
        primitiveTypeNames.add("double");
        primitiveTypeNames.add("boolean");
    }
    

    /** 
     * Returns <code>true</code> if the persistence-modifier of a field
     * having the specified type defaults to <code>true</code>. 
     * @param type the type to be checked
     * @return <code>true</code> if type is a value type; 
     * <code>false</code> otherwise
     */
    public static boolean isPersistenceFieldType(JavaType type)
    {
        return type.isValue() ||
               type.isJDOSupportedCollection() ||
               type.isJDOSupportedMap() ||
               type.isPersistenceCapable() ||
               isPersistenceArrayType(type);
    }
     
    /** 
     * Returns <code>true</code> if the embedded-element property of a field 
     * having the specified type defaults to <code>true</code>.
     * @param type the type to be checked
     * @return <code>true</code> if type is a embedded-element type; 
     * <code>false</code> otherwise
     */
    public static boolean isEmbeddedElementType(JavaType type)
    {
        return !type.isPersistenceCapable() && !type.isInterface();
    }

    /** 
     * Returns <code>true</code> if the embedded property of a field having 
     * the specified type defaults to <code>true</code>.
     * @param type the type to be checked
     * @return <code>true</code> if type is a embedded type; 
     * <code>false</code> otherwise
     */
    public static boolean isEmbeddedFieldType(JavaType type)
    {
        return type.isValue() ||
               isPersistenceArrayType(type);
    }

    /**
     * Returns a JavaType representation for the specified type name. 
     * The method delegates the request to the JavaModel attached to the
     * specified JDOModel. An unqualified name is qualified using first the 
     * specified packagePrefix and then "java.lang.", but only if the type
     * name is the the name of a primitive type. If the method still does
     * not find a valid type, then it returns <code>null</code>.
     * @param jdoModel the owning JDOModel
     * @param typeName the name of the type to be checked
     * @param packagePrefix the package prefix used to qualify the type name
     * @return the JavaType representation of the specified type name or
     * <code>null</code> if it cannot be resolved.
     */
    public static JavaType resolveType(JDOModel jdoModel, String typeName, 
                                       String packagePrefix)
    {
        JavaType type = null;
        JavaModel javaModel = jdoModel.getJavaModel();
        if (primitiveTypeNames.contains(typeName) ||
            (typeName.indexOf('.') != -1) || 
            (packagePrefix == null) || (packagePrefix.length() == 0)) {
            // Take the typeName as specified, 
            // if typeName denotes a primitive type or is a qualified name
            // or if there is no packagePrefix (default package)
            type = javaModel.getJavaType(typeName);
        }
        else {
            // Not a primitive type and not qualified and packagePrefix
            // specified => qualify using packagePrefix
            type = javaModel.getJavaType(packagePrefix + typeName);
            if (type == null) {
                // If type could not be resolved => 
                // use java.lang. package prefix as qualifier 
                type = javaModel.getJavaType("java.lang." + typeName); //NOI18N
            }
        }
        return type;
    }

    /**
     * Returns <code>true</code> if the specified type represents an array
     * and its element type is a value type.
     * @param type the JavaType to be checked
     * @return <code>true</code> if type is a value array; 
     * <code>false</code> otherwise.
     */
    public static boolean isValueArrayType(JavaType type)
    {
        if (type.isArray()) {
            JavaType elementType = type.getArrayComponentType();
            return elementType.isValue();
        }
        return false;
    }
    
    
    //========= Internal helper methods ==========
    
    /**
     * Returns <code>true</code> if the specified type represents an array
     * and its element type is a persistence capable class.
     * @param type the JavaType to be checked
     * @return <code>true</code> if type is a persistent array; 
     * <code>false</code> otherwise.
     */
    private static boolean isPersistenceArrayType(JavaType type)
    {
         if (type.isArray()) {
            JavaType elementType = type.getArrayComponentType();
            return elementType.isValue() ||
                   elementType.isPersistenceCapable();
        }
        return false;
    }
    
}
