/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.stream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.nnip.hello.api.HelloService;
import com.nnip.msg.api.MsgService;
import com.nnip.stream.api.StreamService;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the HelloString.
 */
public class StreamServiceImpl implements StreamService {

  private final HelloService helloService;
//  private final MsgService msgService;

  @Inject
  public StreamServiceImpl(HelloService helloService) {
//  public StreamServiceImpl(HelloService helloService, MsgService msgService) {
    this.helloService = helloService;
//    this.msgService = msgService;
  }

  @Override
  public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> stream() {
    return hellos -> completedFuture(
        hellos.mapAsync(8, name -> helloService.hello(name).invoke()));
  }

}
