/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.hello.impl;

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

import java.util.Optional;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {

  private final PersistentEntityRegistry registry;

  @Inject
  public HelloServiceImpl(PersistentEntityRegistry registry) {
    this.registry = registry;

    registry.register(GreetingEntity.class);

    // TODO Subscribe to the events from the event service.
//    itemService.itemEvents().subscribe().atLeastOnce(Flow.<ItemEvent>create().mapAsync(1, itemEvent -> {
//      if (itemEvent instanceof ItemEvent.AuctionStarted) {
//        ItemEvent.AuctionStarted auctionStarted = (ItemEvent.AuctionStarted) itemEvent;
//        Auction auction = new Auction(auctionStarted.getItemId(), auctionStarted.getCreator(),
//                auctionStarted.getReservePrice(), auctionStarted.getIncrement(), auctionStarted.getStartDate(),
//                auctionStarted.getEndDate());
//
//        return entityRef(auctionStarted.getItemId()).ask(new AuctionCommand.StartAuction(auction));
//      } else if (itemEvent instanceof ItemEvent.AuctionCancelled) {
//        return entityRef(itemEvent.getItemId()).ask(AuctionCommand.CancelAuction.INSTANCE);
//      } else {
//        return CompletableFuture.completedFuture(Done.getInstance());
//      }
//    }));
  }

  @Override
  public ServiceCall<NotUsed, Greeting> placeGreeting(String greeting) {
    return request -> {
      GreetingCommand.AddGreeting placeGreeting = new GreetingCommand.AddGreeting(greeting);
      return entityRef(greeting).ask(placeGreeting).thenApply(result ->
              new Greeting(result.getGreeting()));
    };
  }

  /*@Override
  public ServiceCall<NotUsed, PSequence<Greeting>> getGreetings() {
    return request -> {
      return entityRef(itemId).ask(GreetingCommand.GetGreeting.INSTANCE).thenApply(greet -> {
        List<Greeting> greetings = greet.getGreeting().stream()
                .map(this::convertBid)
                .collect(Collectors.toList());
        return TreePVector.from(greetings);
      });
    };
  }*/

  private PersistentEntityRef<GreetingCommand> entityRef(String greeting) {
    return registry.refFor(GreetingEntity.class, greeting);
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
      return ref.ask(new Greeting(msg));
    };
  }

}
