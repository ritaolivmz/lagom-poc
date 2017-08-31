/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.hello.api;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

import akka.Done;
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

  String GREETINGS_TOPIC = "greeting";

  ServiceCall<NotUsed, String> hello(String id);

  ServiceCall<GreetingMessage, Done> useGreeting(String id);

  ServiceCall<String, String> getPublishedGreeting();

  @Override
  default Descriptor descriptor() {
    return named("helloservice").withCalls(
            pathCall("/api/hello/:id", this::hello),
            pathCall("/api/hello/:id", this::useGreeting),
            Service.call(this::getPublishedGreeting)
    ).publishing(
            Service.topic(GREETINGS_TOPIC, this::greetingsTopic)
    ).withAutoAcl(true);
  }

  Topic<GreetingMessage> greetingsTopic();
}
