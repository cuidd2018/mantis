package com.u002.mantis;

import java.util.List;

public interface Config {

     void start() throws Exception;

     void init();


     static  interface  listener{
    	<T> List<T> onSuccess(Class<T> clazz);

     }

}

