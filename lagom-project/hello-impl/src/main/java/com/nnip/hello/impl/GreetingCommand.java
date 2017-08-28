package com.nnip.hello.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;

/**
 * A greeting command.
 */
public interface GreetingCommand extends Jsonable {

    /**
     * Add a greeting.
     */
    final class AddGreeting implements GreetingCommand, PersistentEntity.ReplyType<AddGreetingResult> {
        private String greeting;

        public AddGreeting(String greeting) {
            this.greeting = greeting;
        }

        public String getGreeting() {
            return greeting;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AddGreeting placeGreeting = (AddGreeting) o;

            if (greeting != placeGreeting.greeting) return false;
            return true;

        }

        @Override
        public int hashCode() {
            int result = greeting.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "AddGreeting{" +
                    "greeting=" + greeting +
                    '}';
        }
    }

    /**
     * The result of adding a greeting.
     */
    final class AddGreetingResult implements Jsonable {
        /**
         * The output message.
         */
        private String outputMessage = "Thank you for adding this greeting to our vocabulary [ dummy ]";
        /**
         * The greeting.
         */
        private final String greeting;

        @JsonCreator
        public AddGreetingResult(String greeting) {
            this.greeting = greeting;
            this.outputMessage = outputMessage.replace("dummy", greeting);
        }

        public String getOutputMessage() {
            return outputMessage;
        }

        public String getGreeting() {
            return greeting;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AddGreetingResult that = (AddGreetingResult) o;

            if (greeting != that.greeting) return false;
            if (outputMessage != that.outputMessage) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = greeting.hashCode();
            result = 31 * result + outputMessage.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "AddGreetingResult{" +
                    "greeting=" + greeting +
                    ", outputMessage=" + outputMessage +
                    '}';
        }
    }

    /**
     * Get the greeting.
     */
    enum GetGreeting implements GreetingCommand, PersistentEntity.ReplyType<Greeting> {
        INSTANCE
    }

    public final class Greeting implements GreetingCommand, PersistentEntity.ReplyType<String> {

        private String greeting;

        public Greeting(String greeting) {
            this.greeting = greeting;
        }
    }
}
