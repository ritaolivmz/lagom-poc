package com.nnip.hello.impl;

/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 *//*

import akka.NotUsed;
import akka.japi.Pair;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

import com.nnip.hello.api.Greeting;
import com.nnip.hello.api.GreetingEvent;
import com.nnip.hello.api.HelloService;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

*/
/**
 * Implementation of the HelloService.
 *//*

public class HelloServiceImpl implements HelloService {

  private final PersistentEntityRegistry registry;

  @Inject
  public HelloServiceImpl(PersistentEntityRegistry registry) {
    this.registry = registry;

    registry.register(GreetingEntity.class);
  }

  @Override
  public ServiceCall<NotUsed, Greeting> placeGreeting(String greeting) {
    return request -> {
      GreetingCommand.AddGreeting placeGreeting = new GreetingCommand.AddGreeting(greeting);
      return entityRef(greeting).ask(placeGreeting).thenApply(result ->
              new Greeting(result.getOutputMessage()));
    };
  }

*/
/*  @Override
  public ServiceCall<NotUsed, PSequence<Greeting>> getGreetings() {
    return request -> {
      return entityRef().ask(GreetingCommand.GetGreeting.INSTANCE).thenApply(greet -> {
        List<Greeting> bids = greet.getBiddingHistory().stream()
                .map(this::convertBid)
                .collect(Collectors.toList());
        return TreePVector.from(bids);
      });
    };
  }*//*


  private PersistentEntityRef<GreetingCommand> entityRef() {
    return registry.refFor(GreetingEntity.class, "all");
  }

  private PersistentEntityRef<GreetingCommand> entityRef(String greeting) {
    return registry.refFor(GreetingEntity.class, greeting);
  }

  public ServiceCall<NotUsed, PSequence<Greeting>> getGreetings() {
    return null;
  }

  @Override
  public Topic<GreetingEvent> greetingsEvents() {
    return TopicProducer.taggedStreamWithOffset(GreetingEvent.TAG.allTags(), this::streamForTag);
  }

  private Source<Pair<GreetingEvent, Offset>, ?> streamForTag(AggregateEventTag<GreetingEvent> tag, Offset offset) {
    return registry.eventStream(tag, offset).filter(eventOffset ->
            eventOffset.first() instanceof GreetingEvent.GreetingPlaced);
  }

  @Override
  public ServiceCall<NotUsed, String> hello(String msg) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<GreetingCommand> ref = registry.refFor(GreetingEntity.class, msg);
      // Ask the entity the Hello command.
      return ref.ask(new GreetingCommand.Greeting(msg));
    };
  }

}
*/

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.nnip.hello.api.GreetingMessage;
import com.nnip.hello.api.HelloService;

import javax.inject.Inject;
import java.util.Optional;

public class HelloServiceImpl implements HelloService {


  private final PersistentEntityRegistry persistentEntityRegistry;

  @Inject
  public HelloServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    persistentEntityRegistry.register(HelloGreeting.class);
  }

  @Override
  public ServiceCall<NotUsed, String> hello(String id) {
    System.out.println("\nHere is the hello()");
    return request -> {
      PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloGreeting.class, id);
      return ref.ask(new HelloCommand.Hello(id, Optional.empty()));
    };
  }

  @Override
  public ServiceCall<GreetingMessage, Done> useGreeting(String id) {
    System.out.println("\nHere is the useGreeting()");
    return request -> {
      PersistentEntityRef<HelloCommand> ref = persistentEntityRegistry.refFor(HelloGreeting.class, id);
      System.out.println("\nref: "+ref.toString());
      return ref.ask(new HelloCommand.UseGreetingMessage(request.message));
    };

  }

  @Override
  public Topic<GreetingMessage> greetingsTopic() {
    return TopicProducer.singleStreamWithOffset(offset -> persistentEntityRegistry.eventStream(HelloEventTag.INSTANCE, offset)
            .map(this::convertEvent));
  }

  private Pair<GreetingMessage, Offset> convertEvent(Pair<HelloEvent, Offset> pair) {
    return new Pair<>(new GreetingMessage(pair.first().getMessage()), pair.second());
  }
}
