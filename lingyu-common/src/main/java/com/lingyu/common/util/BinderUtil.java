package com.lingyu.common.util;

import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

public class BinderUtil {

    public static <T> T getBindResult(Environment environment, String name, Class<T> target) {
        Binder binder = Binder.get(environment);
        BindResult<T> bindResult = binder.bind(name, target);
        return bindResult.get();
    }

}
