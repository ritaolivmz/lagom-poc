/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.hello.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import com.nnip.hello.api.GreetingMessage;
import com.nnip.hello.api.HelloService;
import com.nnip.hello.impl.HelloCommand.*;


/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {

  private final PersistentEntityRegistry persistentEntityRegistry;

  @Inject
  public HelloServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    persistentEntityRegistry.register(HelloEntity.class);
  }

  @Override
  public ServiceCall<NotUsed, String> hello(String id) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloEntity.class, id);
      // Ask the entity the Hello command.
      return ref.ask(new Hello(id, Optional.empty()));
    };
  }

  @Override
  public ServiceCall<String, String> displayHelloMsg(String userId) {
    return request -> CompletableFuture.completedFuture("BOM DIA");
  }

  @Override
  public ServiceCall<NotUsed, String> test(String id, String msg) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloEntity.class, id);
      // Ask the entity the Hello command.
      return ref.ask(new Test(msg, id));
    };
  }

  /*@Override
  public ServiceCall<NotUsed, PrettyPrinter.Item> useGreeting(String greeting, String id) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloEntity.class, id);
      // Ask the entity the Hello command.
      return ref.ask(new UseGreetingMessage(id));
    };
  }*/

  @Override
  public ServiceCall<GreetingMessage, String> useGreeting(String greeting, String id) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloEntity.class, id);
      // Tell the entity to use the greeting message specified.
      return ref.ask(new UseGreetingMessage(request.message));
    };

  }


}
