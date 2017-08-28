package com.nnip.hello.impl;

/**
 * A greeting.
 */
public final class Greeting {
    /**
     * The greeting.
     */
    private final String greeting;

    public Greeting(String greeting) {
        this.greeting = greeting;
    }

    public String getGreeting() {
        return greeting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Greeting greet = (Greeting) o;

        if (greeting != greet.greeting) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = greeting.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Greeting{" +
                "greeting=" + greeting +
                '}';
    }
}