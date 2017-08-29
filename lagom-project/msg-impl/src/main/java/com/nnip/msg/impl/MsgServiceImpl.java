/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.msg.impl;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Flow;

import javax.inject.Inject;

import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.nnip.hello.api.GreetingMessage;
import com.nnip.hello.api.HelloService;
import com.nnip.msg.api.MsgService;


/**
 * Implementation of the HelloService.
 */
public class MsgServiceImpl implements MsgService {

  private final HelloService helloService;
  private String lastMessage;

  @Inject
  public MsgServiceImpl(HelloService helloService) {
    this.helloService = helloService;
    this.helloService.greetingsTopic().subscribe()
            .atLeastOnce(Flow.fromFunction(this::displayMessage));
  }

  private Done displayMessage(GreetingMessage message) {
    System.out.println("[MSG WS] Message :  " + message);
    this.lastMessage = message.message;
    return Done.getInstance();
  }

  @Override
  public ServiceCall<NotUsed, com.nnip.msg.api.GreetingMessage> displayLastMessage() {
    System.out.println("[MSG WS] Last Message :  " + lastMessage);

    return null;
  }
  /*  return request -> {
      com.nnip.msg.api.GreetingMessage greetingMessage = new com.nnip.msg.api.GreetingMessage(lastMessage);
      return entityRef(uuid)
              .ask(new PUserCommand.CreatePUser(user.getName(), user.getEmail(), password))
              .thenApply(done -> Mappers.toApi(Optional.ofNullable(createdUser)));
    };
  }

  private PersistentEntityRef<PUserCommand> entityRef(UUID id) {
    return entityRef(id.toString());
  }

  private PersistentEntityRef<PUserCommand> entityRef(String id) {
    return registry.refFor(PUserEntity.class, id);
  }*/

}
