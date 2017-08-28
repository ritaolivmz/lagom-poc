package com.nnip.hello.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

/**
 * A persisted greeting event.
 */
public interface MessageGreetingEvent extends Jsonable, AggregateEvent<MessageGreetingEvent> {

    int NUM_SHARDS = 4;
    AggregateEventShards<MessageGreetingEvent> TAG = AggregateEventTag.sharded(MessageGreetingEvent.class, NUM_SHARDS);

    @Override
    default AggregateEventTagger<MessageGreetingEvent> aggregateTag() {
        return TAG;
    }

    /**
     * A greeting was placed.
     */
    final class GreetingPlaced implements MessageGreetingEvent {
        /**
         * The greeting.
         */
        private final Greeting greeting;

        @JsonCreator
        public GreetingPlaced(Greeting greeting) {
            this.greeting = greeting;
        }

        public Greeting getGreeting() {
            return greeting;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GreetingPlaced greetingPlaced = (GreetingPlaced) o;

            if (!greeting.equals(greetingPlaced.greeting)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = greeting.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "GreetingPlaced{" +
                    "greeting=" + greeting +
                    '}';
        }
    }
}
