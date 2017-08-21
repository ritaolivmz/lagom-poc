/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.msg.impl;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;

/**
 * This interface defines all the commands that the Hello entity supports.
 * 
 * By convention, the commands should be inner classes of the interface, which
 * makes it simple to get a complete picture of what commands an entity
 * supports.
 */
public interface MsgCommand extends Jsonable {

  /**
   * A command to say hello to someone using the current greeting message.
   * <p>
   * The reply type is String, and will contain the message to say to that
   * person.
   */
  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  public final class Msg implements MsgCommand, PersistentEntity.ReplyType<String> {
    public final String message;
    public final String helloMsg;

    @JsonCreator
    public Msg(String message, String helloMsg) {
      this.message = Preconditions.checkNotNull(message, "message");
      this.helloMsg = Preconditions.checkNotNull(helloMsg, "helloMsg");
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof Msg && equalTo((Msg) another);
    }

    private boolean equalTo(Msg another) {
      return message.equals(another.message);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + message.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("Message").add("message", message).add("helloMsg", helloMsg).toString();
    }
  }

}
