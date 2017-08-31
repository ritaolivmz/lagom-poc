/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.msg.api;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * The Hello service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the Hello.
 */
public interface MsgService extends Service {

  ServiceCall<NotUsed, String> displayLastMessage();

  @Override
  default Descriptor descriptor() {
    // @formatter:off
    return named("msg").withCalls(
            pathCall("/api/msg/displayMessage",  this::displayLastMessage)
      ).withAutoAcl(true);
    // @formatter:on
  }
}
