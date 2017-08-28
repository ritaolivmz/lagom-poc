package com.nnip.hello.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.serialization.Jsonable;
import org.immutables.value.Value;
import org.pcollections.PSequence;

import java.util.Optional;
import java.util.function.Function;

/**
 * The greeting state.
 */
@Value
public final class GreetingState implements Jsonable {
    /**
     * The greeting history.
     */
    private static PSequence<Greeting> greetingsHistory;
    private Greeting greeting;

    @JsonCreator
    public GreetingState(PSequence<Greeting> greetingsHistory) {
        this.greetingsHistory = greetingsHistory;
    }

    @JsonCreator
    public GreetingState(Greeting greeting) {
        this.greeting = greeting;
    }

    public static PSequence<Greeting> getGreetingsHistory() {
        return greetingsHistory;
    }

    public static GreetingState create(Greeting greeting) {
        getGreetingsHistory().plus(greeting);
        return new GreetingState(greeting);
    }

    public GreetingState updateDetails(Greeting greeting) {
        return new GreetingState(greeting);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GreetingState that = (GreetingState) o;

        if (!greetingsHistory.equals(that.greetingsHistory)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = greetingsHistory.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GreetingState{" +
                "greetingsHistory=" + greetingsHistory +
                '}';
    }
}
