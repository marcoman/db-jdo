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

import java.lang.reflect.Field;

import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.util.I18NHelper;



/**
 * This class provides a basic JavaType implementation using a reflection
 * Class instance.
 * <p> 
 * Note, BaseReflectionJavaType must not be used for array types, since it inherits
 * the default implemention of methods isArray and getArrayComponentType
 * from its superclass AbstractJavaType.
 * 
 * @author Michael Bouschen
 * @since JDO 1.1
 */
public class BaseReflectionJavaType
    extends AbstractJavaType
{
    /** The java.lang.Class instance for this BaseReflectionJavaType. */
    protected Class clazz;

    /** The superclass JavaType. */
    protected JavaType superclass;

    /** I18N support */
    private static I18NHelper msg = 
        I18NHelper.getInstance(BaseReflectionJavaType.class);

    /**
     * Constructor. The specified java.lang.Class instance must not be
     * <code>null</code>. The 
     * @param clazz the Class instance representing the type
     * @param superclass JavaType instance representing the superclass.
     */
    public BaseReflectionJavaType(Class clazz, JavaType superclass)
    {
        if (clazz == null)
            throw new ModelFatalException(msg.msg(
                "ERR_InvalidNullClassInstance", "BaseReflectionJavaType.<init>")); //NOI18N
        this.clazz = clazz;
        this.superclass = superclass;
    }
    
    /** 
     * Determines if this JavaType object represents an interface type.
     * @return <code>true</code> if this object represents an interface type; 
     * <code>false</code> otherwise.
     */
    public boolean isInterface()
    {
        return clazz.isInterface();
    }
    
    /** 
     * Returns true if this JavaType is compatible with the specified
     * JavaType. 
     * @param javaType the type this JavaType is checked with.
     * @return <code>true</code> if this is compatible with the specified
     * type; <code>false</code> otherwise.
     */
    public boolean isCompatibleWith(JavaType javaType)
    {
        if (javaType == null)
            return false;
        
        if (javaType instanceof BaseReflectionJavaType) {
            BaseReflectionJavaType otherType = (BaseReflectionJavaType)javaType;
            return otherType.getJavaClass().isAssignableFrom(clazz);
        }
        
        return false;
    }

    /**
     * Returns the name of the type. If this type represents a class or
     * interface, the name is fully qualified.
     * @return type name
     */
    public String getName()
    {
        return clazz.getName();
    }

    /**
     * Returns the Java language modifiers for the field represented by
     * this JavaType, as an integer. The java.lang.reflect.Modifier class
     * should be used to decode the modifiers. 
     * @return the Java language modifiers for this JavaType
     */
    public int getModifiers() 
    { 
        return clazz.getModifiers(); 
    }

    /** 
     * Returns the JavaType representing the superclass of the entity
     * represented by this JavaType. If this JavaType represents either the 
     * Object class, an interface, a primitive type, or <code>void</code>, 
     * then <code>null</code> is returned. If this object represents an
     * array class then the JavaType instance representing the Object class
     * is returned.  
     * @return the superclass of the class represented by this JavaType.
     */
    public JavaType getSuperclass()
    {
        return superclass;
    }

    /**
     * Returns a JavaField instance that reflects the field with the
     * specified name of the class or interface represented by this
     * JavaType instance. The method returns <code>null</code>, if the
     * class or interface (or one of its superclasses) does not have a
     * field with that name.
     * @param fieldName the name of the field 
     * @return the JavaField instance for the specified field in this class
     * or <code>null</code> if there is no such field.
     */
    public JavaField getJavaField(String fieldName) 
    { 
        Field field = 
            BaseReflectionJavaField.getDeclaredFieldPrivileged(clazz, fieldName);
        if (field != null) {
            return new BaseReflectionJavaField(field, this);
        }
        
        // check superclass, if available and other than Object
        JavaType superclass = getSuperclass();
        if ((superclass != null) && (superclass != PredefinedType.objectType)) {
            return superclass.getJavaField(fieldName);
        }
        
        return null;
    }

    // ===== Methods not defined in JavaType =====

    /**
     * Returns the java.lang.Class instance wrapped by this JavaType.
     * @return the Class instance for this JavaType.
     */
    public Class getJavaClass()
    {
        return clazz;
    }

}
