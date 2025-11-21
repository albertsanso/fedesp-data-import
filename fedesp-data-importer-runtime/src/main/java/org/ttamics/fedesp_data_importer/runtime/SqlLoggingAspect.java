package org.ttamics.fedesp_data_importer.runtime;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.springframework.stereotype.Component;

import java.util.logging.*;

@Aspect
@Component
public class SqlLoggingAspect {

    @Before("execution(* org.ttamics.fedesp_data_importer.*.*(..))")
    public void enableSqlLogging() {
        Logger.getLogger("org.hibernate.SQL").setLevel(Level.INFO);
        Logger.getLogger("org.hibernate.orm.jdbc.bind").setLevel(Level.INFO);
    }

    @After("execution(* org.ttamics.fedesp_data_importer.*.*(..))")
    public void disableSqlLogging() {
        Logger.getLogger("org.hibernate.SQL").setLevel(Level.OFF);
        Logger.getLogger("org.hibernate.orm.jdbc.bind").setLevel(Level.OFF);
    }
}
