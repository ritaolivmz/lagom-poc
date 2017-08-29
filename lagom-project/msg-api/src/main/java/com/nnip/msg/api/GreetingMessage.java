package com.nnip.msg.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable @JsonDeserialize public final class GreetingMessage {

  public final String greeting;
  public final String msg;

  @JsonCreator public GreetingMessage(String message) {
    this.msg = Preconditions.checkNotNull(message, "msg");
    this.greeting = "Olá, ";
  }

  @JsonCreator public GreetingMessage(String greeting, String message) {
    this.greeting = Preconditions.checkNotNull(greeting, "greeting");
    this.msg = Preconditions.checkNotNull(message, "msg");
  }

  @Override public boolean equals(@Nullable Object another) {
    return this == another || another instanceof GreetingMessage && equalTo((GreetingMessage) another);
  }

  private boolean equalTo(GreetingMessage another) {
    return msg.equals(another.msg);
  }

  @Override public int hashCode() {
    int h = 31;
    h = h * 17 + msg.hashCode();
    return h;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper("GreetingMessage").add("greeting", greeting).add("message", msg).toString();
  }
}
