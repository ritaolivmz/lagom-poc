package com.nnip.hello.impl;

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
  public ServiceCall<String, String> getPublishedGreeting() {
    System.out.println("[HELLO WS] getPublishedGreeting invoked");
    return (request -> {
      System.out.println("[HELLO WS] id is : "+request);
      System.out.println("TEST RESULT: "+entityRef(request)
              .ask(new HelloCommand.GetGreeting(request)));
      return entityRef(request)
              .ask(new HelloCommand.GetGreeting(request));
    });
  }

  @Override
  public Topic<GreetingMessage> greetingsTopic() {
    return TopicProducer.singleStreamWithOffset(offset -> persistentEntityRegistry.eventStream(HelloEventTag.INSTANCE, offset)
            .map(this::convertEvent));
  }

  private Pair<GreetingMessage, Offset> convertEvent(Pair<HelloEvent, Offset> pair) {
    System.out.println("[HELLO WS] pair first : "+pair.first().getMessage());
    System.out.println("[HELLO WS] pair second : "+pair.second());
    return new Pair<>(new GreetingMessage(pair.first().getMessage()), pair.second());
  }

  private PersistentEntityRef<HelloCommand> entityRef(String id) {
    return persistentEntityRegistry.refFor(HelloGreeting.class, id);
  }

}
