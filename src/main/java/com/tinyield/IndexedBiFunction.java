package com.tinyield;

/**
 * Represents a bi-function that also accepts an int index.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(int, Object, Object)}.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 */

@FunctionalInterface
public interface IndexedBiFunction<T,U,R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param index
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(int index, T t, U u);
}
