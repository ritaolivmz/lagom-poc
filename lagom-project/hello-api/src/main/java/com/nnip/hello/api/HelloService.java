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

/**
 * The Hello service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the Hello.
 */
public interface HelloService extends Service {

  /**
   * Example: curl http://localhost:9000/api/hello/Alice
   */
  ServiceCall<NotUsed, String> hello(String id);

  ServiceCall<NotUsed, String> test(String id, String msg);

  ServiceCall<String, String> displayHelloMsg(String id);

  /**
   * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
   * "Hi"}' http://localhost:9000/api/hello/Alice
   */
  ServiceCall<GreetingMessage, String> useGreeting(String greeting, String id);

  @Override
  default Descriptor descriptor() {
    // @formatter:off
    return named("hello").withCalls(
        pathCall("/api/hello/:id",  this::hello),
            pathCall("/api/greet/:id",  this::displayHelloMsg),
        pathCall("/api/hello/:greeting/greeting/:id", this::useGreeting),
            pathCall("/api/test/:id/:msg", this::test)
      ).withAutoAcl(true);
    // @formatter:on
  }
}
