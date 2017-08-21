/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.msg.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.nnip.hello.api.HelloService;
import com.nnip.msg.api.MsgService;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class MsgModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindService(MsgService.class, MsgServiceImpl.class);
    bindClient(HelloService.class);
  }
}
