/*
 * Copyright 2018-2019 OAAT's author : Frédéric Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

module oaat {
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.validation;
    requires jjwt;
    requires kotlin.logging;
    requires kotlin.stdlib;
    requires org.commonmark;
    requires org.commonmark.ext.autolink;
    requires reactor.core;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.data.commons;
    requires spring.data.mongodb;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.security.web;
    requires spring.web;
    requires spring.webflux;
}
