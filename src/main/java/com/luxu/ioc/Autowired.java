package com.luxu.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xulu on 2017/8/31.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    public String name() default "";
}
