/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.msg.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

import com.nnip.hello.api.HelloService;
import com.nnip.msg.api.MsgService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of the HelloService.
 */
public class MsgServiceImpl implements MsgService {

  private final PersistentEntityRegistry persistentEntityRegistry;
  private final HelloService helloService;

  @Inject
  public MsgServiceImpl(PersistentEntityRegistry persistentEntityRegistry, HelloService helloService) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    persistentEntityRegistry.register(MsgEntity.class);
    this.helloService = helloService;
  }

  @Override
  public ServiceCall<NotUsed, String> msg(String msg) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<MsgCommand> ref = persistentEntityRegistry.refFor(MsgEntity.class, msg);
      // Ask the entity the Hello command.
      CompletableFuture<String> helloMsg =
              (CompletableFuture<String>) helloService.displayHelloMsg().invoke();
      try {
        return CompletableFuture.completedFuture("Message from Msg Service: " + msg + "\nMessage from Hello Service: " + helloMsg.get()
                +"\n\n\n"+helloMsg.get()+", "+msg+"!");
      } catch (InterruptedException | ExecutionException e) {
        return CompletableFuture.completedFuture("An exception occurred.");
      }
    };
  }

}
