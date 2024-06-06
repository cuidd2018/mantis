package com.u002.mantis;

import java.util.Collection;
import java.util.List;

public interface Container {

    public <T> Collection<T> findBeans(Class<T> clazz);
}
