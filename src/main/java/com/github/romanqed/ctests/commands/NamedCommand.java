package com.github.romanqed.ctests.commands;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@IndexAnnotated
public @interface NamedCommand {
    String value();
}
