/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
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
 * SingleFieldIdentity.java
 *
 */
 
package javax.jdo.identity;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/** This class is the abstract base class for all single field identity
 * classes. A common case of application identity uses exactly one 
 * persistent field in the class to represent identity. In this case, 
 * the application can use a standard JDO class instead of creating 
 * a new user-defined class for the purpose.
 * @version 2.0
 */
public abstract class SingleFieldIdentity
    implements Externalizable {
    
    /** The class of the target object.
     */
    transient private Class targetClass;
    
    /** The name of the class of the target object.
     */
    private String targetClassName;

    /** The hashCode.
     */
    protected int hashCode;

    /** The single character that separates the class name from the key value
     * in String representations of SingleFieldIdentity and its subclasses.
     */
    public static final char SEPARATOR_CHARACTER = ':';
    
    /** Constructor with target class.
     * @param pcClass the class of the target
     * @since 2.0
     */
    protected SingleFieldIdentity(Class pcClass) {
        if (pcClass == null)
            throw new NullPointerException();
        targetClass = pcClass;
        targetClassName = pcClass.getName();
    }

    /** Constructor only for Externalizable.
     */
    public SingleFieldIdentity () {
    }

    /** Return the target class.
     * @return the target class.
     * @since 2.0
     */
    public Class getTargetClass() {
        return targetClass;
    }

    /** Return the target class name.
     * @return the target class name.
     * @since 2.0
     */
    public String getTargetClassName() {
        return targetClassName;
    }

    /** Check the class and class name and object type. If restored
     * from serialization, class will be null so compare class name.
     * @param obj the other object
     * @return true if the class or class name is the same
     * @since 2.0
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            SingleFieldIdentity other = (SingleFieldIdentity) obj;
            if (targetClass != null && targetClass == other.targetClass)
                return true;
            return targetClassName.equals (other.targetClassName);
        }
    }

    /** Return the hash code of the class name.
     * @return the hash code of the class name
     */
    protected int hashClassName() {
        return targetClassName.hashCode();
    }
    
    /** Return the cached hash code.
     * @return the cached hash code.
     */
    public int hashCode() {
        return hashCode;
    }
    
    /** Convert the toString representation of a single field identity
     * to remove the class name. The format of the toString is
     * <targetClass name:id>.
     */
    protected static String justTheId(String str) {
        return str.substring(str.indexOf(':') + 1);
    }

    /** Write to the output stream.
     * @param out the stream
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(targetClassName);
        out.writeInt(hashCode);
    }

    /** Read from the input stream.
     * @return a new instance with the target class name set
     * @since 2.0
     */
    public void readExternal(ObjectInput in)
            throws IOException, ClassNotFoundException {
        targetClass = null;
        targetClassName = (String)in.readObject();
        hashCode = in.readInt();
    }
}
