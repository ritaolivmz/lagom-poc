package com.nnip.hello.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public class HelloGreeting extends PersistentEntity<HelloCommand, HelloEvent, WorldState> {

    @Override
    public Behavior initialBehavior(Optional<WorldState> snapshotState) {

        BehaviorBuilder b = newBehaviorBuilder(
                snapshotState.orElse(new WorldState("Hello", LocalDateTime.now().toString())));

        b.setCommandHandler(HelloCommand.UseGreetingMessage.class, (cmd, ctx) ->
                ctx.thenPersist(new HelloEvent.GreetingMessageChanged(cmd.message),
                        // Then once the event is successfully persisted, we respond with done.
                        evt -> ctx.reply(Done.getInstance())));

        b.setEventHandler(HelloEvent.GreetingMessageChanged.class,
                evt -> new WorldState(evt.message, LocalDateTime.now().toString()));

        b.setReadOnlyCommandHandler(HelloCommand.Hello.class,
                (cmd, ctx) -> ctx.reply(state().message + ", " + cmd.name + "!"));

        return b.build();
    }

}
