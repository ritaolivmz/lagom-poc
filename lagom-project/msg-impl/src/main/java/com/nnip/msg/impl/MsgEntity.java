/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.nnip.msg.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

/**
 * This is an event sourced entity. It has a state, {@link MsgState}, which
 * stores what the greeting should be (eg, "Hello").
 * <p>
 * Event sourced entities are interacted with by sending them commands. This
 * used to change the greeting, and a {@link com.nnip.msg.impl.MsgCommand.Msg} command, which is a read
 * only command which returns a greeting to the name specified by the command.
 * <p>
 * Commands get translated to events, and it's the events that get persisted by
 * the entity. Each event will have an event handler registered for it, and an
 * event handler simply applies an event to the current state. This will be done
 * when the event is first created, and it will also be done when the entity is
 * loaded from the database - each event will be replayed to recreate the state
 * of the entity.
 * <p>
 * This entity defines one event, the {@link} event,
 */
public class MsgEntity extends PersistentEntity<MsgCommand, MsgEvent, MsgState> {

  /**
   * An entity can define different behaviours for different states, but it will
   * always start with an initial behaviour. This entity only has one behaviour.
   */
  @Override
  public Behavior initialBehavior(Optional<MsgState> snapshotState) {

    /*
     * Behaviour is defined using a behaviour builder. The behaviour builder
     * starts with a state, if this entity supports snapshotting (an
     * optimisation that allows the state itself to be persisted to combine many
     * events into one), then the passed in snapshotState may have a value that
     * can be used.
     *
     * Otherwise, the default state is to use the Hello greeting.
     */
    BehaviorBuilder b = newBehaviorBuilder(
        snapshotState.orElse(new MsgState("Message", LocalDateTime.now().toString())));

    /*
     * Event handler for the GreetingMessageChanged event.
     */
    /*b.setEventHandler(MessageChanged.class,
        // We simply update the current state to use the greeting message from
        // the event.
        evt -> new MsgState(evt.message, LocalDateTime.now().toString()));*/

    b.setReadOnlyCommandHandler(MsgCommand.Msg.class,
            // Get the greeting from the current state, and prepend it to the name
            // that we're sending
            // a greeting to, and reply with that message.
            /*(cmd, ctx) -> ctx.reply(state().message + ", " + cmd.helloMsg + " !!"));*/
            (cmd, ctx) -> ctx.reply(Done.getInstance())
    );

    /*
     * We've defined all our behaviour, so build and return it.
     */
    return b.build();
  }

}
