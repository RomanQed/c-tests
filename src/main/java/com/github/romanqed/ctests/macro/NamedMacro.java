package com.github.romanqed.ctests.macro;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@IndexAnnotated
public @interface NamedMacro {
    String value();
}
