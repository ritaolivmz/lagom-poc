package com.nnip.hello.impl;

import akka.NotUsed;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide.completedStatements;

/**
 * The greeting persistent entity.
 */
public class GreetingEntity extends PersistentEntity<GreetingCommand, MessageGreetingEvent, NotUsed> {

    private PreparedStatement insertGreeting;

    @Override
    public Behavior initialBehavior(Optional<NotUsed> snapshotState) {
        BehaviorBuilder builder = newBehaviorBuilder(NotUsed.getInstance());

        /* handler for the add greeting command */
        builder.setCommandHandler(GreetingCommand.AddGreeting.class, this::handleAddGreeting);

        /* handler for the PlaceGreeting event */
        /*builder.setEventHandler(MessageGreetingEvent.GreetingPlaced.class,
                e -> insertGreeting(GreetingState.create(e.getGreeting())));*/
        return builder.build();
    }

    /**
     * Add new greeting.
     */
    private Persist<MessageGreetingEvent> handleAddGreeting(GreetingCommand.AddGreeting greetingObject, CommandContext<GreetingCommand.AddGreetingResult> ctx) {
        return ctx.thenPersist(new MessageGreetingEvent.GreetingPlaced(new Greeting(greetingObject.getGreeting())), (e) ->
                ctx.reply(new GreetingCommand.AddGreetingResult(greetingObject.getGreeting()))
        );
    }

   /* private Behavior insertGreeting(GreetingState state) {
        BehaviorBuilder builder = newBehaviorBuilder(NotUsed.getInstance());

        builder.setEventHandler(MessageGreetingEvent.GreetingPlaced.class, updateGreeting(state));

        return builder.build();
    }
*/
    private Function<MessageGreetingEvent.GreetingPlaced, GreetingState> updateGreeting(GreetingState state) {
        return (evt) -> state.updateDetails(evt.getGreeting());
    }

}