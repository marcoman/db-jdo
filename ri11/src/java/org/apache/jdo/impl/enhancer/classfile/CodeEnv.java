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


package org.apache.jdo.impl.enhancer.classfile;

import java.util.Hashtable;

/**
 * Environment in which to decode the attributes of a CodeAttribute.
 */
class CodeEnv {
    /* The constant pool */
    private ConstantPool constantPool;

    /* hash table mapping byte code offset to InsnTarget */
    private Hashtable targets = new Hashtable(7);

    CodeEnv(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    final InsnTarget getTarget(int offset) {
        Integer off = new Integer(offset);
        InsnTarget targ = (InsnTarget)targets.get(off);
        if (targ == null) {
            targ = new InsnTarget(offset);
            targets.put(off, targ);
        }
        return targ;
    }

    final InsnTarget findTarget(int offset) {
        Integer off = new Integer(offset);
        return (InsnTarget)targets.get(off);
    }

    final ConstantPool pool() {
        return constantPool;
    }
}
