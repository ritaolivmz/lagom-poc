/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.msg.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;
import com.lightbend.lagom.javadsl.immutable.ImmutableStyle;

import org.immutables.value.Value;
import java.time.Instant;

/**
 * This interface defines all the events that the Hello entity supports.
 * <p>
 * By convention, the events should be inner classes of the interface, which
 * makes it simple to get a complete picture of what events an entity has.
 */
public interface MsgEvent extends Jsonable, AggregateEvent<MsgEvent> {

  @Value.Immutable
  @ImmutableStyle
  @JsonDeserialize
  interface AbstractOrderCreated extends MsgEvent {
    @Override
    default AggregateEventTag<MsgEvent> aggregateTag() {
      return MsgEventTag.INSTANCE;
    }

    @Value.Parameter
    MsgCommand.Msg getMsg();

    @Value.Default
    default Instant getTimestamp() {
      return Instant.now();
    }
  }

/*  *//**
   * An event that represents a change in greeting message.
   *//*
  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  public final class MessageChanged implements MsgEvent {
    public final String message;

    @JsonCreator
    public MessageChanged(String message) {
      this.message = Preconditions.checkNotNull(message, "message");
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof MessageChanged && equalTo((MessageChanged) another);
    }

    private boolean equalTo(MessageChanged another) {
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
      return MoreObjects.toStringHelper("MessageChanged").add("message", message).toString();
    }
  }*/
}
