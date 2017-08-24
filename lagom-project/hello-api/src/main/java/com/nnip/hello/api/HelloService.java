/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.hello.api;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;

/**
 * The Hello service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the Hello.
 */
public interface HelloService extends Service {

  ServiceCall<NotUsed, String> hello(String id);

  /**
   * Place a greeting.
   */
  ServiceCall<NotUsed, Greeting> placeGreeting(String greeting);

  /**
   * Get all of the greetings.
   */
  /*ServiceCall<NotUsed, PSequence<Greeting>> getGreetings();*/

  /**
   * The greeting events topic.
   */
  Topic<GreetingEvent> greetingsEvents();

  String TOPIC_ID = "hello-GreetingEvent";

  @Override
  default Descriptor descriptor() {
    return named("hello").withCalls(
            pathCall("/api/hello/:id",  this::hello),
            pathCall("/api/hello/add/:greeting", this::placeGreeting)
            /*pathCall("/api/greet/greetings", this::getGreetings)*/
    ).publishing(
            Service.topic(TOPIC_ID, this::greetingsEvents)
    ).withAutoAcl(true);
  }
}
