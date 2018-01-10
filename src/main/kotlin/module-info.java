module one.at.a.time {
    requires kotlin.stdlib;
    requires spring.boot.autoconfigure;
    requires spring.data.mongodb;
    requires spring.boot;
    requires spring.context;
    requires spring.security.core;
    requires reactor.core;
    requires commonmark;
    requires commonmark.ext.autolink;
    requires spring.webflux;
    requires spring.web;
    requires spring.security.config;
    requires spring.data.commons;
    requires java.validation;
    requires spring.security.web;

    exports one.at.a.time;
}