/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.msg.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

import com.lightbend.lagom.javadsl.pubsub.PubSubRef;
import com.lightbend.lagom.javadsl.pubsub.PubSubRegistry;
import com.lightbend.lagom.javadsl.pubsub.TopicId;
import com.nnip.hello.api.HelloService;
import com.nnip.msg.api.MsgService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of the HelloService.
 */
public class MsgServiceImpl implements MsgService {

  private final PersistentEntityRegistry persistentEntityRegistry;
  private final HelloService helloService;
  private final PubSubRegistry pubSubRegistry;

  @Inject
  public MsgServiceImpl(PersistentEntityRegistry persistentEntityRegistry, HelloService helloService, PubSubRegistry topics) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    persistentEntityRegistry.register(MsgEntity.class);
    this.helloService = helloService;
    this.pubSubRegistry = topics;
  }

  /*@Override
  public ServiceCall<NotUsed, String> msg(String msg) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<MsgCommand> ref = persistentEntityRegistry.refFor(MsgEntity.class, msg);
      // Ask the entity the Hello command.
      CompletableFuture<String> helloMsg =
              (CompletableFuture<String>) helloService.displayHelloMsg(msg).invoke();
      try {
        return CompletableFuture.completedFuture("Message from Msg Service: " + msg + "\nMessage from Hello Service: " + helloMsg.get()
                +"\n\n\n"+helloMsg.get()+", "+msg+"!");
      } catch (InterruptedException | ExecutionException e) {
        return CompletableFuture.completedFuture("An exception occurred.");
      }
    };
  }*/

  @Override
  public ServiceCall<NotUsed, Source<String, ?>> retrieveMsg(String msg) {
    return request -> {
      final PubSubRef<String> topic =
              pubSubRegistry.refFor(TopicId.of(String.class, "topic"));
      return CompletableFuture.completedFuture(topic.subscriber());
    };
  }

}
