/*
 Copyright (C) 2010-2016 by FuzzyLite Limited.
 All rights reserved.

 This file is part of jfuzzylite(TM).

 jfuzzylite is free software: you can redistribute it and/or modify it under
 the terms of the FuzzyLite License included with the software.

 You should have received a copy of the FuzzyLite License along with
 jfuzzylite. If not, see <http://www.fuzzylite.com/license/>.

 fuzzylite(R) is a registered trademark of FuzzyLite Limited.
 jfuzzylite(TM) is a trademark of FuzzyLite Limited.

 */
package com.fuzzylite.factory;

import com.fuzzylite.Op;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConstructionFactory<T> implements Op.Cloneable {

    private Map<String, Class<? extends T>> constructors;

    public ConstructionFactory() {
        this.constructors = new HashMap<String, Class<? extends T>>();
    }

    public void register(Class<? extends T> clazz) {
        this.register(clazz.getSimpleName(), clazz);
    }

    public void register(String simpleName, Class<? extends T> clazz) {
        this.constructors.put(simpleName, clazz);
    }

    public void deregister(String simpleName) {
        this.constructors.remove(simpleName);
    }

    public boolean hasConstructor(String simpleName) {
        return this.constructors.containsKey(simpleName);
    }

    public Set<String> available() {
        return new HashSet<String>(this.constructors.keySet());
    }

    public T constructObject(String simpleName) {
        if (simpleName == null) {
            return null;
        }

        if (this.constructors.containsKey(simpleName)) {
            try {
                Class<? extends T> clazz = this.constructors.get(simpleName);
                if (clazz != null) {
                    return clazz.newInstance();
                }
                return null;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        throw new RuntimeException("[construction error] constructor <" + simpleName
                + "> not registered in " + getClass().getSimpleName());
    }

    public Map<String, Class<? extends T>> getConstructors() {
        return constructors;
    }

    public void setConstructors(Map<String, Class<? extends T>> constructors) {
        this.constructors = constructors;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ConstructionFactory<T> clone() throws CloneNotSupportedException {
        ConstructionFactory<T> result = (ConstructionFactory<T>) super.clone();
        result.constructors = new HashMap<String, Class<? extends T>>(this.constructors);
        return result;
    }

}
