package com.nnip.hello.impl;

import akka.Done;
import akka.stream.javadsl.Flow;
import com.google.inject.Singleton;
import com.nnip.hello.api.GreetingMessage;
import com.nnip.hello.api.HelloService;

import javax.inject.Inject;

@Singleton
public class Consumer {

  private final HelloService helloService;

  @Inject public Consumer(HelloService helloService) {
    this.helloService = helloService;
    /*this.helloService.greetingsTopic().subscribe()
        .atLeastOnce(Flow.fromFunction(this::displayMessage));*/
  }

  private Done displayMessage(GreetingMessage message) {
    System.out.println("Message :  " + message);
    return Done.getInstance();
  }

}