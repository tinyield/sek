package com.tinyield;

import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.Grouping;
import kotlin.collections.IndexedValue;
import kotlin.random.Random;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static kotlin.sequences.SequencesKt.generateSequence;
import static kotlin.sequences.SequencesKt.sequenceOf;

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
interface IndexedBiFunction<T,U,R> {

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

@SuppressWarnings("squid:S3252")
public interface Sek<T> extends Sequence<T> {

    /**
     * Creates a sequence that returns the specified values.
     *
     * @param elements values to be yielded by the sequence
     * @return a Sek instance that will yield the provided {@param elements}
     */
    @SafeVarargs
    static <T> Sek<T> of(T... elements) {
        return sequenceOf(elements)::iterator;
    }

    /**
     * Creates a sequence that returns the specified values.
     *
     * @param elements values to be yielded by the sequence
     * @return a Sek instance that will yield the provided {@param elements}
     */
    static <T> Sek<T> of(Iterable<T> elements) {
        return elements::iterator;
    }

    /**
     * Returns a sequence which invokes the function to calculate the next value on each iteration until the function returns `null`.
     *
     * @param gen Supplier that generates values to be yielded by this sequence
     * @return a Sek instance that will yield the elements provided by {@param gen}
     * <p>
     * The returned sequence is constrained to be iterated only once.
     * <p>
     * see {@code constrainOnce}
     */
    static <T> Sek<T> generate(Supplier<T> gen) {
        return generateSequence(gen::get)::iterator;
    }

    /**
     * @return an empty sequence.
     */
    static <T> Sek<T> empty() {
        return SequencesKt.<T>emptySequence()::iterator;
    }

    /**
     * @param predicate used to test elements of this {@code Sek}
     * @return true if all elements match the given {@param predicate}, false otherwise
     * <p>
     * The operation is _terminal_.
     */
    default boolean all(Predicate<? super T> predicate) {
        return SequencesKt.all(this, predicate::test);
    }

    /**
     * @return true if sequence has at least one element, false otherwise
     * <p>
     * The operation is _terminal_.
     */
    default boolean any() {
        return SequencesKt.any(this);
    }

    /**
     * @param predicate used to test elements of this {@code Sek}
     * @return true if sequence has at least one element matches the given {@param predicate}, false otherwise
     * <p>
     * The operation is _terminal_.
     *
     */
    default boolean any(Predicate<? super T> predicate) {
        return SequencesKt.any(this, predicate::test);
    }

    /**
     * Creates an {@link Iterable} instance that wraps the original {@code Sek} returning its elements when being iterated.
     * @return an {@link Iterable} wrapping the original {@code Sek}.
     */
    default Iterable<T> asIterable() {
        return SequencesKt.asIterable(this);
    }

    /**
     * @return this {@code Sek} as a Kotlin {@link Sequence}.
     */
    default Sequence<T> asSequence() {
        return this;
    }

    /**
     * @return this {@code Sek} as a {@link Stream}.
     */
    default Stream<T> asStream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.iterator(), 0),false);
    }

    /**
     * @param transform Function that maps elements into key-value pairs.
     * @return a {@link Map} containing key-value pairs provided by {@param transform} function
     * applied to elements of the given {@code Sek}.
     * <p>
     * If any of two pairs would have the same key the last one gets added to the map.
     * <p>
     * The returned map preserves the entry iteration order of the original sequence.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <K, V> Map<K, V> associate(Function<? super T, Pair<K, V>> transform) {
        return SequencesKt.associate(this, transform::apply);
    }

    /**
     * @param keySelector Function that maps elements into a key
     * @return a {@link Map} containing the elements from the given sequence indexed by the key
     * returned from {@param keySelector} function applied to each element.
     * <p>
     * If any two elements would have the same key returned by {@param keySelector} the last one gets added to the map.
     * <p>
     * The returned map preserves the entry iteration order of the original sequence.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <K> Map<K, T> associateBy(Function<? super T, ? extends K> keySelector) {
        return SequencesKt.associateBy(this, keySelector::apply);
    }

    /**
     * @return a {@link Map} containing the values provided by {@param valueTransform} and indexed by {@param keySelector} functions applied to elements of the given sequence.
     * <p>
     * If any two elements would have the same key returned by {@param keySelector} the last one gets added to the map.
     * <p>
     * The returned map preserves the entry iteration order of the original sequence.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <K, V> Map<K, V> associateBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueTransform) {
        return SequencesKt.associateBy(this, keySelector::apply, valueTransform::apply);
    }

    /**
     * Populates and returns the {@param destination} mutable map with key-value pairs,
     * where key is provided by the {@param keySelector} function applied to each element of the given sequence
     * and value is the element itself.
     * <p>
     * If any two elements would have the same key returned by {@param keySelector} the last one gets added to the map.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <K, M extends Map<K, T>> M associateByTo(M destination, Function<? super T, ? extends K> keySelector) {
        return SequencesKt.associateByTo(this, destination, keySelector::apply);
    }

    /**
     * Populates and returns the {@param destination} mutable map with key-value pairs,
     * where key is provided by the {@param keySelector} function and
     * and value is provided by the {@param valueTransform} function applied to elements of the given sequence.
     * <p>
     * If any two elements would have the same key returned by {@param keySelector} the last one gets added to the map.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <K, V, M extends Map<K, V>> M associateByTo(M destination, Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueTransform) {
        return SequencesKt.associateByTo(this, destination, keySelector::apply, valueTransform::apply);
    }

    /**
     * Populates and returns the {@param destination} mutable map with key-value pairs
     * provided by {@param transform} function applied to each element of the given sequence.
     * <p>
     * If any of two pairs would have the same key the last one gets added to the map.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <K, V, M extends Map<K, V>> M associateTo(M destination, Function<? super T, Pair<K, V>> transform) {
        return SequencesKt.associateTo(this, destination, transform::apply);
    }

    /**
     * @return a {@link Map} where keys are elements from the given sequence and values are
     * produced by the {@param valueSelector} function applied to each element.
     * <p>
     * If any two elements are equal, the last one gets added to the map.
     * <p>
     * The returned map preserves the entry iteration order of the original sequence.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <V> Map<T, V> associateWith(Function<? super T, ? extends V> valueSelector) {
        return SequencesKt.associateWith(this, valueSelector::apply);
    }

    /**
     * Populates and returns the {@param destination} mutable map with key-value pairs for each element of the given sequence,
     * where key is the element itself and value is provided by the {@param valueSelector} function applied to that key.
     * <p>
     * If any two elements are equal, the last one overwrites the former value in the map.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <V, M extends Map<T, V>> M associateWithTo(M destination, Function<? super T, ? extends V> valueSelector) {
        return SequencesKt.associateWithTo(this, destination, valueSelector::apply);
    }

    /**
     * Splits this sequence into a sequence of lists each not exceeding the given {@param size}.
     * <p>
     * The last list in the resulting sequence may have less elements than the given {@param size}.
     *
     * @param size the number of elements to take in each list, must be positive and can be greater than the number of elements in this sequence.
     *             <p>
     *             The operation is _intermediate_ and _stateful_.
     */
    default Sek<List<T>> chunked(int size) {
        return SequencesKt.chunked(this, size)::iterator;
    }

    /**
     * Splits this sequence into several lists each not exceeding the given {@param size}
     * and applies the given {@param transform} function to an each.
     *
     * @param size the number of elements to take in each list, must be positive and can be greater than the number of elements in this sequence.
     *             <p>
     *             The operation is _intermediate_ and _stateful_.
     * @return sequence of results of the {@param transform} applied to an each list.
     * <p>
     * Note that the list passed to the {@param transform} function is ephemeral and is valid only inside that function.
     * You should not store it or allow it to escape in some way, unless you made a snapshot of it.
     * The last list may have less elements than the given {@param size}.
     */
    default <R> Sek<R> chunked(int size, Function<? super List<? extends T>, ? extends R> transform) {
        return SequencesKt.<T, R>chunked(this, size, transform::apply)::iterator;
    }

    /**
     * @return a wrapper sequence that provides values of this sequence, but ensures it can be iterated only one time.
     * <p>
     * The operation is _intermediate_ and _stateless_.
     * <p>
     * {@link IllegalStateException} is thrown on iterating the returned sequence from the second time.
     */
    default Sek<T> constrainOnce() {
        return SequencesKt.constrainOnce(this)::iterator;
    }

    /**
     * @return `true` if {@param element} is found in the sequence.
     * <p>
     * The operation is _terminal_.
     */
    default boolean contains(T element) {
        return SequencesKt.contains(this, element);
    }

    /**
     * @return the number of elements in this sequence.
     * <p>
     * The operation is _terminal_.
     */
    default int count() {
        return SequencesKt.count(this);
    }

    /**
     * @return the number of elements matching the given {@param predicate}.
     * <p>
     * The operation is _terminal_.
     */
    default int count(Predicate<? super T> predicate) {
        return SequencesKt.count(this, predicate::test);
    }

    /**
     * @return a sequence containing only distinct elements from the given sequence.
     * <p>
     * Among equal elements of the given sequence, only the first one will be present in the resulting sequence.
     * The elements in the resulting sequence are in the same order as they were in the source sequence.
     * <p>
     * The operation is _intermediate_ and _stateful_.
     *
     */
    default Sek<T> distinct() {
        return SequencesKt.distinct(this)::iterator;
    }

    /**
     * @param selector Function that calculates the key for each element
     * @return a sequence containing only elements from the given sequence
     * having distinct keys returned by the given {@param selector} function.
     * <p>
     * Among elements of the given sequence with equal keys, only the first one will be present in the resulting sequence.
     * The elements in the resulting sequence are in the same order as they were in the source sequence.
     * <p>
     * The operation is _intermediate_ and _stateful_.
     *
     */
    default <K> Sek<T> distinctBy(Function<? super T, ? extends K> selector) {
        return SequencesKt.distinctBy(this, selector::apply)::iterator;
    }

    /**
     * @param n number of starting elements to discard
     * @return a sequence containing all elements except first {@param n} elements.
     * <p>
     * The operation is _intermediate_ and _stateless_.
     *
     * @throws IllegalArgumentException if {@param n} is negative.
     */
    default Sek<T> drop(int n) {
        return SequencesKt.drop(this, n)::iterator;
    }

    /**
     * @return a sequence containing all elements except first elements that satisfy the given {@param predicate}.
     * <p>
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default Sek<T> dropWhile(Predicate<? super T> predicate) {
        return SequencesKt.dropWhile(this, predicate::test)::iterator;
    }

    /**
     * @return an element at the given {@param index} or throws an {@link IndexOutOfBoundsException} if the {@param index} is out of bounds of this sequence.
     * <p>
     * The operation is _terminal_.
     *
     */
    default T elementAt(int index) {
        return SequencesKt.elementAt(this, index);
    }

    /**
     * @return an element at the given {@param index} or the result of calling the {@param defaultValue} function if the {@param index} is out of bounds of this sequence.
     * <p>
     * The operation is _terminal_.
     *
     */
    default T elementAtOrElse(int index, IntFunction<? extends T> defaultValue) {
        return SequencesKt.elementAtOrElse(this, index, defaultValue::apply);
    }

    /**
     * @return an element at the given {@param index} or `null` if the {@param index} is out of bounds of this sequence.
     * <p>
     * The operation is _terminal_.
     *
     */
    default T elementAtOrNull(int index) {
        return SequencesKt.elementAtOrNull(this, index);
    }

    /**
     * @return a sequence containing only elements matching the given {@param predicate}.
     * <p>
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default Sek<T> filter(Predicate<? super T> predicate) {
        return SequencesKt.filter(this, predicate::test)::iterator;
    }

    /**
     * Appends all elements matching the given {@param predicate} to the given {@param destination}.
     *
     * @param destination Collection in which the elements will be stored
     * @param predicate   Predicate that takes an element and returns the result of
     *                    the predicate evaluation on the element.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <C extends Collection<T>> C filterTo(C destination, Predicate<? super T> predicate) {
        return SequencesKt.filterTo(this, destination, predicate::test);
    }

    /**
     * @return a sequence containing only elements matching the given {@param predicate}.
     *
     * @param predicate   BiPredicate that takes the index of an element and the element itself
     *                    and returns the result of predicate evaluation on the element.
     *                    <p>
     *                    The operation is _intermediate_ and _stateless_.
     */
    default Sek<T> filterIndexed(BiPredicate<Integer, ? super T> predicate) {
        return SequencesKt.filterIndexed(this, predicate::test)::iterator;
    }

    /**
     * Appends all elements matching the given predicate to the given destination.
     *
     * @param destination Collection in which the elements will be stored
     * @param predicate   BiPredicate that takes the index of an element and the element itself
     *                    and returns the result of predicate evaluation on the element.
     *                    <p>
     *                    The operation is _terminal_.
     */
    default <C extends Collection<T>> C filterIndexedTo(C destination, BiPredicate<Integer, ? super T> predicate) {
        return SequencesKt.filterIndexedTo(this, destination, predicate::test);
    }

    /**
     * @return a sequence containing all elements that are instances of specified class.
     * <p>
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<R> filterIsInstance(Class<R> klass) {
        return SequencesKt.filterIsInstance(this, klass)::iterator;
    }

    /**
     * Appends all elements that are instances of specified class to the given {@param destination}.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <R, C extends Collection<R>> C filterIsInstanceTo(C destination, Class<R> klass) {
        return SequencesKt.filterIsInstanceTo(this, destination, klass);
    }

    /**
     * @return a sequence containing all elements not matching the given {@param predicate}.
     * <p>
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default Sek<T> filterNot(Predicate<? super T> predicate) {
        return SequencesKt.filterNot(this, predicate::test)::iterator;
    }

    /**
     * Appends all elements not matching the given {@param predicate} to the given {@param destination}.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <C extends Collection<T>> C filterNotTo(C destination, Predicate<? super T> predicate) {
        return SequencesKt.filterNotTo(this, destination, predicate::test);
    }

    /**
     * @return a sequence containing all elements that are not `null`.
     * <p>
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default Sek<T> filterNotNull() {
        return SequencesKt.filterNotNull(this)::iterator;
    }

    /**
     * Appends all elements that are not `null` to the given {@param destination}.
     * <p>
     * The operation is _terminal_.
     *
     */
    default <C extends Collection<T>> C filterNotNullTo(C destination) {
        return SequencesKt.filterNotNullTo(this, destination);
    }

    /**
     * @return first element.
     *
     * @throws java.util.NoSuchElementException if the sequence is empty.
     *                                  <p>
     *                                  The operation is _terminal_.
     */
    default T first() {
        return SequencesKt.first(this);
    }

    /**
     * @return the first element matching the given {@param predicate}.
     *
     * @throws java.util.NoSuchElementException if no such element is found.
     *                                  <p>
     *                                  The operation is _terminal_.
     */
    default T first(Predicate<? super T> predicate) {
        return SequencesKt.first(this, predicate::test);
    }

    /**
     * @return the first element, or `null` if the sequence is empty.
     * <p>
     * The operation is _terminal_.
     */
    default T firstOrNull() {
        return SequencesKt.firstOrNull(this);
    }

    /**
     * @return the first element matching the given {@param predicate}, or `null` if element was not found.
     * <p>
     * The operation is _terminal_.
     */
    default T firstOrNull(Predicate<? super T> predicate) {
        return SequencesKt.firstOrNull(this, predicate::test);
    }

    /**
     * @return a sequence of all elements from all sequences in this sequence.
     * @throws java.lang.ClassCastException if T is not a Sequence<R>
     *
     * The operation is _intermediate_ and _stateless_.
     */
    @SuppressWarnings("unchecked")
    default <R> Sek<R> flatten() {
        return SequencesKt.flatten((Sequence<Sequence<R>>)this)::iterator;
    }

    /**
     * @return a single sequence of all elements from results of {@param transform} function being invoked on each element of original sequence.
     * <p>
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<R> flatMap(Function<? super T, Sequence<R>> transform) {
        return SequencesKt.flatMap(this, transform::apply)::iterator;
    }

    /**
     * Appends all elements yielded from results of {@param transform} function being invoked on each element of original sequence, to the given {@param destination}.
     * <p>
     * The operation is _terminal_.
     */
    default <R, C extends Collection<? super R>> C flatMapTo(C destination, Function<? super T, Sequence<R>> transform) {
        return SequencesKt.flatMapTo(this, destination, transform::apply);
    }

    /**
     * Accumulates value starting with {@param initial} value and applying {@param operation} from left to right
     * to current accumulator value and each element.
     * <p>
     * @return the specified {@param initial} value if the sequence is empty.
     *
     * @param operation function that takes current accumulator value and an element, and calculates the next accumulator value.
     *                    <p>
     *                    The operation is _terminal_.
     */
    default <R> R fold(R initial, BiFunction<? super R, ? super T, ? extends R> operation) {
        return SequencesKt.fold(this, initial, operation::apply);
    }

    /**
     * Accumulates value starting with {@param initial} value and applying {@param operation} from left to right
     * to current accumulator value and each element with its index in the original sequence.
     * <p>
     * @return the specified {@param initial} value if the sequence is empty.
     *
     * @param operation function that takes the index of an element, current accumulator value
     *                    and the element itself, and calculates the next accumulator value.
     *                    <p>
     *                    The operation is _terminal_.
     */
    default <R> R foldIndexed(R initial, IndexedBiFunction<? super R, ? super T, ? extends R> operation) {
        return SequencesKt.foldIndexed(this, initial, operation::apply);
    }

    /**
     * Performs the given {@param action} on each element.
     * <p>
     * The operation is _terminal_.
     */
    default void forEach(Consumer<? super T> action) {
        SequencesKt.forEach(this, elem -> {
            action.accept(elem);
            return Unit.INSTANCE;
        });
    }

    /**
     * Performs the given {@param action} on each element, providing sequential index with the element.
     *
     * @param action function that takes the index of an element and the element itself
     *                 and performs the action on the element.
     *                 <p>
     *                 The operation is _terminal_.
     */
    default void forEachIndexed(BiConsumer<Integer, ? super T> action) {
        SequencesKt.forEachIndexed(this, (index, elem) -> {
            action.accept(index, elem);
            return Unit.INSTANCE;
        });
    }

    /**
     * Groups elements of the original sequence by the key returned by the given {@param keySelector} function
     * applied to each element and returns a map where each group key is associated with a list of corresponding elements.
     *
     * The returned map preserves the entry iteration order of the keys produced from the original sequence.
     *
     * The operation is _terminal_.
     *
     */
    default <K> Map<K, List<T>> groupBy(Function<? super T, ? extends K> keySelector) {
        return SequencesKt.groupBy(this, keySelector::apply);
    }

    /**
     * Groups elements of the original sequence by the key returned by the given {@param keySelector} function
     * applied to each element and puts to the {@param destination} map each group key associated with a list of corresponding elements.
     *
     * @return The {@param destination} map.
     *
     * The operation is _terminal_.
     *
     */
    default <K, M extends Map<K, List<T>>> M groupByTo(M destination, Function<? super T, ? extends K> keySelector) {
        return SequencesKt.groupByTo(this, destination, keySelector::apply);
    }

    /**
     * Groups values returned by the {@param valueTransform} function applied to each element of the original sequence
     * by the key returned by the given {@param keySelector} function applied to the element
     * and returns a map where each group key is associated with a list of corresponding values.
     *
     * The returned map preserves the entry iteration order of the keys produced from the original sequence.
     *
     * The operation is _terminal_.
     *
     */
    default <K, V> Map<K, List<V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueTransform) {
        return SequencesKt.groupBy(this, keySelector::apply, valueTransform::apply);
    }

    /**
     * Groups values returned by the {@param valueTransform} function applied to each element of the original sequence
     * by the key returned by the given {@param keySelector} function applied to the element
     * and puts to the {@param destination} map each group key associated with a list of corresponding values.
     *
     * @return The {@param destination} map.
     *
     * The operation is _terminal_.
     *
     */
    default <K, V, M extends Map<K, List<V>>> M groupByTo(M destination, Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueTransform) {
        return SequencesKt.groupByTo(this, destination, keySelector::apply, valueTransform::apply);
    }

    /**
     * Creates a {@link Grouping} source from a sequence to be used later with one of group-and-fold operations
     * using the specified {@param keySelector} function to extract a key from each element.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <K> Grouping<T,K> groupingBy(Function<? super T, ? extends K> keySelector) {
        return SequencesKt.groupingBy(this, keySelector::apply);
    }

    /**
     * @return a sequence that iterates through the elements either of this sequence
     * or, if this sequence turns out to be empty, of the sequence returned by {@param defaultValue} function.
     *
     */
    default Sek<T> ifEmpty(Supplier<Sek<? extends T>> defaultValue) {
        return SequencesKt.ifEmpty(this, defaultValue::get)::iterator;
    }

    /**
     * @return first index of {@param element}, or -1 if the sequence does not contain element.
     *
     * The operation is _terminal_.
     */
    default int indexOf(T element) {
        return SequencesKt.indexOf(this, element);
    }

    /**
     * @return index of the first element matching the given {@param predicate}, or -1 if the sequence does not contain such element.
     *
     * The operation is _terminal_.
     */
    default int indexOfFirst(Predicate<? super T> predicate) {
        return SequencesKt.indexOfFirst(this, predicate::test);
    }

    /**
     * @return index of the last element matching the given {@param predicate}, or -1 if the sequence does not contain such element.
     *
     * The operation is _terminal_.
     */
    default int indexOfLast(Predicate<? super T> predicate) {
        return SequencesKt.indexOfLast(this, predicate::test);
    }

    /**
     * Appends the string from all the elements separated using {@param separator} and using the given {@param prefix} and {@param postfix} if supplied.
     *
     * If the collection could be huge, you can specify a non-negative value of {@param limit}, in which case only the first {@param limit}
     * elements will be appended, followed by the {@param truncated} string.
     *
     * The operation is _terminal_.
     *
     */
    default <A extends Appendable> A  joinTo(A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix, int limit, CharSequence truncated, Function<? super T, ? extends CharSequence> transform) {
        return SequencesKt.joinTo(this, buffer, separator, prefix, postfix, limit, truncated, transform::apply);
    }

    /**
     * Creates a string from all the elements separated using {@param separator} and using the given {@param prefix} and {@param postfix} if supplied.
     *
     * If the collection could be huge, you can specify a non-negative value of {@param limit}, in which case only the first {@param limit}
     * elements will be appended, followed by the {@param truncated} string.
     *
     * The operation is _terminal_.
     *
     */
    default String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix, int limit, CharSequence truncated, Function<? super T, ? extends CharSequence> transform) {
        return SequencesKt.joinToString(this, separator, prefix, postfix, limit, truncated, transform::apply);
    }

    /**
     * @return the last element.
     *
     * The operation is _terminal_.
     *
     * @throws java.util.NoSuchElementException if the sequence is empty.
     *
     */
    default T last() {
        return SequencesKt.last(this);
    }

    /**
     * @return the last element matching the given {@param predicate}.
     *
     * The operation is _terminal_.
     *
     * @throws java.util.NoSuchElementException if no such element is found.
     *
     */
    default T last(Predicate<? super T> predicate) {
        return SequencesKt.last(this, predicate::test);
    }

    /**
     * @return last index of {@param element}, or -1 if the sequence does not contain element.
     *
     * The operation is _terminal_.
     */
    default int lastIndexOf(T element) {
        return SequencesKt.lastIndexOf(this, element);
    }

    /**
     * @return the last element, or `null` if the sequence is empty.
     *
     * The operation is _terminal_.
     *
     */
    default T lastOrNull() {
        return SequencesKt.lastOrNull(this);
    }

    /**
     * @return the last element matching the given {@param predicate}, or `null` if no such element was found.
     *
     * The operation is _terminal_.
     *
     */
    default T lastOrNull(Predicate<? super T> predicate) {
        return SequencesKt.lastOrNull(this, predicate::test);
    }

    /**
     * @return a sequence containing the results of applying the given {@param transform} function
     * to each element in the original sequence.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<R> map(Function<? super T, R> transform) {
        return SequencesKt.map(this, transform::apply)::iterator;
    }

    /**
     * Applies the given {@param transform} function to each element of the original sequence
     * and appends the results to the given {@param destination}.
     *
     * The operation is _terminal_.
     */
    default <R, C extends Collection<R>> C mapTo(C destination, Function<? super T, R> transform) {
        return SequencesKt.mapTo(this, destination, transform::apply);
    }

    /**
     * @return a sequence containing the results of applying the given {@param transform} function
     * to each element and its index in the original sequence.
     * @param transform function that takes the index of an element and the element itself
     * and returns the result of the transform applied to the element.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default <R> Sek<R> mapIndexed(BiFunction<Integer, ? super T, R> transform) {
        return SequencesKt.mapIndexed(this, transform::apply)::iterator;
    }

    /**
     * Applies the given {@param transform} function to each element and its index in the original sequence
     * and appends the results to the given {@param destination}.
     * @param transform function that takes the index of an element and the element itself
     * and returns the result of the transform applied to the element.
     *
     * The operation is _terminal_.
     */
    default <R, C extends Collection<R>> C mapIndexedTo(C destination, BiFunction<Integer, ? super T, R> transform) {
        return SequencesKt.mapIndexedTo(this, destination, transform::apply);
    }

    /**
     * @return a sequence containing only the non-null results of applying the given {@param transform} function
     * to each element and its index in the original sequence.
     * @param transform function that takes the index of an element and the element itself
     * and returns the result of the transform applied to the element.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default <R> Sek<R> mapIndexedNotNull(BiFunction<Integer, ? super T, R> transform) {
        return SequencesKt.mapIndexedNotNull(this, transform::apply)::iterator;
    }

    /**
     * Applies the given {@param transform} function to each element and its index in the original sequence
     * and appends only the non-null results to the given {@param destination}.
     * @param transform function that takes the index of an element and the element itself
     * and returns the result of the transform applied to the element.
     *
     * The operation is _terminal_.
     */
    default <R, C extends Collection<R>> C mapIndexedNotNullTo(C destination,BiFunction<Integer, ? super T, R> transform) {
        return SequencesKt.mapIndexedNotNullTo(this, destination,transform::apply);
    }

    /**
     * @return a sequence containing only the non-null results of applying the given {@param transform} function
     * to each element in the original sequence.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<R> mapNotNull(Function<? super T, R> transform) {
        return SequencesKt.mapNotNull(this, transform::apply)::iterator;
    }

    /**
     * Applies the given {@param transform} function to each element in the original sequence
     * and appends only the non-null results to the given {@param destination}.
     *
     * The operation is _terminal_.
     */
    default <R, C extends Collection<R>> C mapNotNullTo(C destination,Function<? super T, R> transform) {
        return SequencesKt.mapNotNullTo(this,destination, transform::apply);
    }

    /**
     * @return the first element yielding the largest value of the given function or `null` if there are no elements.
     *
     * The operation is _terminal_.
     *
     */
    default <R extends Comparable<R>> T maxByOrNull(Function<? super T,? extends R> selector) {
        return SequencesKt.maxByOrNull(this, selector::apply);
    }


    /**
     * @return the first element having the largest value according to the provided {@param comparator} or `null` if there are no elements.
     *
     * The operation is _terminal_.
     */
    default T maxWithOrNull(Comparator<? super T> comparator) {
        return SequencesKt.maxWithOrNull(this, comparator);
    }

    /**
     * @return the first element yielding the smallest value of the given function or `null` if there are no elements.
     *
     * The operation is _terminal_.
     *
     */
    default <R extends Comparable<R>> T minByOrNull(Function<? super T,? extends R> selector) {
        return SequencesKt.minByOrNull(this, selector::apply);
    }

    /**
     * @return a sequence containing all elements of the original sequence without the first occurrence of the given {@param element}.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default Sek<T> minus(T element) {
        return SequencesKt.minus(this, element)::iterator;
    }

    /**
     * @return a sequence containing all elements of original sequence except the elements contained in the given {@param elements} array.
     *
     * Note that the source sequence and the array being subtracted are iterated only when an `iterator` is requested from
     * the resulting sequence. Changing any of them between successive calls to `iterator` may affect the result.
     *
     * The {@param elements} array may be converted to a {@link HashSet} to speed up the operation, thus the elements are required to have
     * a correct and stable implementation of `hashCode()` that doesn't change between successive invocations.
     *
     * The operation is _intermediate_ and _stateful_.
     */
    default Sek<T> minus(T[] elements) {
        return SequencesKt.minus(this, elements)::iterator;
    }

    /**
     * @return a sequence containing all elements of original sequence except the elements contained in the given {@param elements} collection.
     *
     * Note that the source sequence and the collection being subtracted are iterated only when an `iterator` is requested from
     * the resulting sequence. Changing any of them between successive calls to `iterator` may affect the result.
     *
     * The {@param elements} collection may be converted to a {@link HashSet} to speed up the operation, thus the elements are required to have
     * a correct and stable implementation of `hashCode()` that doesn't change between successive invocations.
     *
     * The operation is _intermediate_ and _stateful_.
     */
    default Sek<T> minus(Iterable<? extends T> elements) {
        return SequencesKt.minus(this, elements)::iterator;
    }

    /**
     * @return a sequence containing all elements of original sequence except the elements contained in the given {@param elements} sequence.
     *
     * Note that the source sequence and the sequence being subtracted are iterated only when an `iterator` is requested from
     * the resulting sequence. Changing any of them between successive calls to `iterator` may affect the result.
     *
     * The operation is _intermediate_ for this sequence and _terminal_ and _stateful_ for the {@param elements} sequence.
     *
     * The {@param elements} sequence may be converted to a {@link HashSet} to speed up the operation, thus the elements are required to have
     * a correct and stable implementation of `hashCode()` that doesn't change between successive invocations.
     */
    default Sek<T> minus(Sequence<? extends T> elements) {
        return SequencesKt.minus(this, elements)::iterator;
    }

    /**
     * @return the first element having the smallest value according to the provided {@param comparator} or `null` if there are no elements.
     *
     * The operation is _terminal_.
     */
    default T minWithOrNull(Comparator<? super T> comparator) {
        return SequencesKt.minWithOrNull(this, comparator);
    }

    /**
     * @return `true` if the sequence has no elements.
     *
     * The operation is _terminal_.
     *
     */
    default boolean none() {
        return SequencesKt.none(this);
    }

    /**
     * @return `true` if no elements match the given {@param predicate}.
     *
     * The operation is _terminal_.
     *
     */
    default boolean none(Predicate<? super T> predicate) {
        return SequencesKt.none(this, predicate::test);
    }

    /**
     * @return a sequence which performs the given {@param action} on each element of the original sequence as they pass through it.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default Sek<T> onEach(Consumer<? super T> action) {
        return SequencesKt.onEach(this, elem -> {
            action.accept(elem);
            return Unit.INSTANCE;
        })::iterator;
    }

    /**
     * @return a sequence which performs the given {@param action} on each element of the original sequence as they pass through it.
     * @param action function that takes the index of an element and the element itself
     * and performs the action on the element.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default Sek<T> onEachIndexed(BiConsumer<Integer, ? super T> action) {
        return SequencesKt.onEachIndexed(this, (index, elem) -> {
            action.accept(index, elem);
            return Unit.INSTANCE;
        })::iterator;
    }

    /**
     * Splits the original sequence into pair of lists,
     * where *first* list contains elements for which {@param predicate} yielded `true`,
     * while *second* list contains elements for which {@param predicate} yielded `false`.
     *
     * The operation is _terminal_.
     *
     */
    default Pair<List<T>, List<T>> partition(Predicate<? super T> predicate) {
        return SequencesKt.partition(this, predicate::test);
    }

    /**
     * @return a sequence containing all elements of the original sequence and then the given {@param element}.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default Sek<T> plus(T element) {
        return SequencesKt.plus(this, element)::iterator;
    }

    /**
     * @return a sequence containing all elements of original sequence and then all elements of the given {@param elements} array.
     *
     * Note that the source sequence and the array being added are iterated only when an `iterator` is requested from
     * the resulting sequence. Changing any of them between successive calls to `iterator` may affect the result.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default Sek<T> plus(T[] elements) {
        return SequencesKt.plus(this, elements)::iterator;
    }

    /**
     * @return a sequence containing all elements of original sequence and then all elements of the given {@param elements} collection.
     *
     * Note that the source sequence and the collection being added are iterated only when an `iterator` is requested from
     * the resulting sequence. Changing any of them between successive calls to `iterator` may affect the result.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default Sek<T> plus(Iterable<? extends T> elements) {
        return SequencesKt.plus(this, elements)::iterator;
    }

    /**
     * @return a sequence containing all elements of original sequence and then all elements of the given {@param elements} sequence.
     *
     * Note that the source sequence and the sequence being added are iterated only when an `iterator` is requested from
     * the resulting sequence. Changing any of them between successive calls to `iterator` may affect the result.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default Sek<T> plus(Sequence<? extends T> elements) {
        return SequencesKt.plus(this, elements)::iterator;
    }

    /**
     * Accumulates value starting with the first element and applying {@param operation} from left to right
     * to current accumulator value and each element.
     *
     * Throws an exception if this sequence is empty. If the sequence can be empty in an expected way,
     * please use {@code reduceOrNull} instead. It returns `null` when its receiver is empty.
     *
     * @param operation function that takes current accumulator value and an element,
     * and calculates the next accumulator value.
     *
     * The operation is _terminal_.
     *
     */
    default T reduce(BinaryOperator<T> operation) {
        return SequencesKt.reduce(this, operation::apply);
    }

    /**
     * Accumulates value starting with the first element and applying {@param operation} from left to right
     * to current accumulator value and each element with its index in the original sequence.
     *
     * Throws an exception if this sequence is empty. If the sequence can be empty in an expected way,
     * please use {@code reduceIndexedOrNull} instead. It returns `null` when its receiver is empty.
     *
     * @param operation function that takes the index of an element, current accumulator value and the element itself,
     * and calculates the next accumulator value.
     *
     * The operation is _terminal_.
     *
     */
    default T reduceIndexed(IndexedBiFunction<? super T, ? super T, ? extends T> operation) {
        return SequencesKt.reduceIndexed(this, operation::apply);
    }

    /**
     * Accumulates value starting with the first element and applying {@param operation} from left to right
     * to current accumulator value and each element with its index in the original sequence.
     *
     * @return `null` if the sequence is empty.
     *
     * @param operation function that takes the index of an element, current accumulator value and the element itself,
     * and calculates the next accumulator value.
     *
     * The operation is _terminal_.
     *
     */
    default T reduceIndexedOrNull(IndexedBiFunction<? super T, ? super T, ? extends T> operation) {
        return SequencesKt.reduceIndexedOrNull(this, operation::apply);
    }

    /**
     * Accumulates value starting with the first element and applying {@param operation} from left to right
     * to current accumulator value and each element.
     *
     * @return `null` if the sequence is empty.
     *
     * @param operation function that takes current accumulator value and an element,
     * and calculates the next accumulator value.
     *
     * The operation is _terminal_.
     *
     */
    default T reduceOrNull(BinaryOperator<T> operation) {
        return SequencesKt.reduceOrNull(this, operation::apply);
    }

    /**
     * @return an original collection containing all the non-`null` elements, throwing an {@link IllegalArgumentException} if there are any `null` elements.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default Sek<T> requireNoNulls() {
        return SequencesKt.requireNoNulls(this)::iterator;
    }

    /**
     * @return a sequence containing successive accumulation values generated by applying {@param operation} from left to right
     * to each element and current accumulator value that starts with {@param initial} value.
     *
     * Note that `acc` value passed to {@param operation} function should not be mutated;
     * otherwise it would affect the previous value in resulting sequence.
     * The {@param initial} value should also be immutable (or should not be mutated)
     * as it may be passed to {@param operation} function later because of sequence's lazy nature.
     *
     * @param operation function that takes current accumulator value and an element, and calculates the next accumulator value.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<R> runningFold(R initial, BiFunction<? super R, ? super T, ? extends R> operation) {
        return SequencesKt.runningFold(this, initial, operation::apply)::iterator;
    }

    /**
     * @return a sequence containing successive accumulation values generated by applying {@param operation} from left to right
     * to each element, its index in the original sequence and current accumulator value that starts with {@param initial} value.
     *
     * Note that `acc` value passed to {@param operation} function should not be mutated;
     * otherwise it would affect the previous value in resulting sequence.
     * The {@param initial} value should also be immutable (or should not be mutated)
     * as it may be passed to {@param operation} function later because of sequence's lazy nature.
     *
     * @param operation function that takes the index of an element, current accumulator value
     * and the element itself, and calculates the next accumulator value.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<R> runningFoldIndexed(R initial, IndexedBiFunction<? super R, ? super T, ? extends R> operation) {
        return SequencesKt.runningFoldIndexed(this, initial, operation::apply)::iterator;
    }

    /**
     * @return a sequence containing successive accumulation values generated by applying {@param operation} from left to right
     * to each element and current accumulator value that starts with the first element of this sequence.
     *
     * Note that `acc` value passed to {@param operation} function should not be mutated;
     * otherwise it would affect the previous value in resulting sequence.
     *
     * @param operation function that takes current accumulator value and the element, and calculates the next accumulator value.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default Sek<T> runningReduce(BinaryOperator<T> operation) {
        return SequencesKt.runningReduce(this, operation::apply)::iterator;
    }

    /**
     * @return a sequence containing successive accumulation values generated by applying {@param operation} from left to right
     * to each element, its index in the original sequence and current accumulator value that starts with the first element of this sequence.
     *
     * Note that `acc` value passed to {@param operation} function should not be mutated;
     * otherwise it would affect the previous value in resulting sequence.
     *
     * @param operation function that takes the index of an element, current accumulator value
     * and the element itself, and calculates the next accumulator value.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default Sek<T> runningReduceIndexed(IndexedBiFunction<? super T, ? super T, ? extends T> operation) {
        return SequencesKt.runningReduceIndexed(this, operation::apply)::iterator;
    }

    /**
     * @return a sequence containing successive accumulation values generated by applying {@param operation} from left to right
     * to each element and current accumulator value that starts with {@param initial} value.
     *
     * Note that `acc` value passed to {@param operation} function should not be mutated;
     * otherwise it would affect the previous value in resulting sequence.
     * The {@param initial} value should also be immutable (or should not be mutated)
     * as it may be passed to {@param operation} function later because of sequence's lazy nature.
     *
     * @param operation function that takes current accumulator value and an element, and calculates the next accumulator value.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<R> scan(R initial, BiFunction<? super R, ? super T, ? extends R> operation) {
        return SequencesKt.scan(this, initial, operation::apply)::iterator;
    }

    /**
     * @return a sequence containing successive accumulation values generated by applying {@param operation} from left to right
     * to each element, its index in the original sequence and current accumulator value that starts with {@param initial} value.
     *
     * Note that `acc` value passed to {@param operation} function should not be mutated;
     * otherwise it would affect the previous value in resulting sequence.
     * The {@param initial} value should also be immutable (or should not be mutated)
     * as it may be passed to {@param operation} function later because of sequence's lazy nature.
     *
     * @param operation function that takes the index of an element, current accumulator value
     * and the element itself, and calculates the next accumulator value.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<R> scanIndexed(R initial, IndexedBiFunction<? super R, ? super T, ? extends R> operation) {
        return SequencesKt.scanIndexed(this, initial, operation::apply)::iterator;
    }

    /**
     * @return a sequence that yields elements of this sequence randomly shuffled.
     *
     * Note that every iteration of the sequence returns elements in a different order.
     *
     * The operation is _intermediate_ and _stateful_.
     */
    default Sek<T> shuffled() {
        return SequencesKt.shuffled(this)::iterator;
    }

    /**
     * @return a sequence that yields elements of this sequence randomly shuffled
     * using the specified {@param random} instance as the source of randomness.
     *
     * Note that every iteration of the sequence returns elements in a different order.
     *
     * The operation is _intermediate_ and _stateful_.
     */
    default Sek<T> shuffled(Random random) {
        return SequencesKt.shuffled(this, random)::iterator;
    }

    /**
     * @return the single element, or throws an exception if the sequence is empty or has more than one element.
     *
     * The operation is _terminal_.
     */
    default T single() {
        return SequencesKt.single(this);
    }

    /**
     * @return the single element matching the given {@param predicate}, or throws exception if there is no or more than one matching element.
     *
     * The operation is _terminal_.
     */
    default T single(Predicate<? super T> predicate) {
        return SequencesKt.single(this, predicate::test);
    }

    /**
     * @return single element, or `null` if the sequence is empty or has more than one element.
     *
     * The operation is _terminal_.
     */
    default T singleOrNull() {
        return SequencesKt.singleOrNull(this);
    }

    /**
     * @return the single element matching the given {@param predicate}, or `null` if element was not found or more than one element was found.
     *
     * The operation is _terminal_.
     */
    default T singleOrNull(Predicate<? super T> predicate) {
        return SequencesKt.singleOrNull(this, predicate::test);
    }

    /**
     * @return a sequence that yields elements of this sequence sorted according to their natural sort order.
     * @throws java.lang.ClassCastException if T does not implement {@link Comparable}
     * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
     *
     * The operation is _intermediate_ and _stateful_.
     */
    @SuppressWarnings("unchecked")
    default Sek<T> sorted() {
        return ((Sequence<T>) SequencesKt.sorted((Sequence<Comparable<Object>>) this))::iterator;
    }

    /**
     * @param selector Function that maps each element to a comparable value
     * @return a sequence that yields elements of this sequence sorted according to natural sort order of the value
     * returned by specified {@param selector} function.
     *
     * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
     *
     * The operation is _intermediate_ and _stateful_.
     *
     */
    default <R extends Comparable<? super R>> Sek<T> sortedBy(Function<? super T, ? extends R> selector) {
        return SequencesKt.sortedBy(this, selector::apply)::iterator;
    }

    /**
     * @param selector Function that maps each element to a comparable value
     * @return a sequence that yields elements of this sequence sorted descending according to natural sort order of the
     * value returned by specified {@param selector} function.
     *
     * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
     *
     * The operation is _intermediate_ and _stateful_.
     */
    default <R extends Comparable<? super R>> Sek<T> sortedByDescending(Function<? super T, ? extends R> selector) {
        return SequencesKt.sortedByDescending(this, selector::apply)::iterator;
    }

    /**
     * @return a sequence that yields elements of this sequence sorted descending according to their natural sort order.
     * @throws java.lang.ClassCastException if T does not implement {@link Comparable}
     *
     * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
     *
     * The operation is _intermediate_ and _stateful_.
     */
    @SuppressWarnings("unchecked")
    default Sek<T> sortedDescending() {
        return ((Sequence<T>) SequencesKt.sortedDescending((Sequence<Comparable<Object>>) this))::iterator;
    }

    /**
     * @return a sequence that yields elements of this sequence sorted according to the specified {@param comparator}.
     *
     * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
     *
     * The operation is _intermediate_ and _stateful_.
     */
    default Sek<T> sortedWith(Comparator<T> comparator) {
        return SequencesKt.sortedWith(this, comparator)::iterator;
    }

    /**
     * @param selector ToIntFunction that calculates the value for each element
     * @return the sum of all values produced by {@param selector} function applied to each element in the sequence.
     *
     * The operation is _terminal_.
     */
    default int sumBy(ToIntFunction<T> selector) {
        return SequencesKt.sumBy(this, selector::applyAsInt);
    }

    /**
     * @param selector ToDoubleFunction that calculates the value for each element
     * @return the sum of all values produced by {@param selector} function applied to each element in the sequence.
     *
     * The operation is _terminal_.
     */
    default double sumByDouble(ToDoubleFunction<T> selector) {
        return SequencesKt.sumByDouble(this, selector::applyAsDouble);
    }

    /**
     * @return a sequence containing first {@param n} elements.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     * @throws IllegalArgumentException if {@param n} is negative.
     *
     */
    default Sek<T> take(int n) {
        return SequencesKt.take(this, n)::iterator;
    }

    /**
     * @return a sequence containing first elements satisfying the given {@param predicate}.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default Sek<T> takeWhile(Predicate<? super T> predicate) {
        return SequencesKt.takeWhile(this, predicate::test)::iterator;
    }

    /**
     * This method allows for easy extensibility of the Sek API with user defined operations.
     *
     * @param next A Function that transforms the current Sek into the next by adding
     *             the user defined operation to the pipeline
     * @param <R>  Type of the next Sek
     * @return A Sek representing this Sek with the operation added by {@param next}.
     */
    default <R> Sek<R> then(Function<Sek<T>, Sequence<R>> next) {
        return next.apply(this)::iterator;
    }

    /**
     * Appends all elements to the given {@param destination} collection.
     *
     * The operation is _terminal_.
     */
    default <C extends Collection<T>> C toCollection(C destination) {
        return SequencesKt.toCollection(this, destination);
    }

    /**
     * @return a new {@link HashSet} of all elements.
     *
     * The operation is _terminal_.
     */
    default HashSet<T> toHashSet() {
        return SequencesKt.toHashSet(this);
    }

    /**
     * @return a {@link List} containing all elements.
     *
     * The operation is _terminal_.
     */
    default List<T> toList() {
        return SequencesKt.toList(this);
    }

    /**
     * @return a new {@link List} filled with all elements of this sequence.
     *
     * The operation is _terminal_.
     */
    default List<T> toMutableList() {
        return SequencesKt.toMutableList(this);
    }

    /**
     * @return a new {@link Set} containing all distinct elements from the given sequence.
     *
     * The returned set preserves the element iteration order of the original sequence.
     *
     * The operation is _terminal_.
     */
    default Set<T> toMutableSet() {
        return SequencesKt.toMutableSet(this);
    }

    /**
     * @return a {@link Set} of all elements.
     *
     * The returned set preserves the element iteration order of the original sequence.
     *
     * The operation is _terminal_.
     */
    default Set<T> toSet() {
        return SequencesKt.toSet(this);
    }

    /**
     * @return a new {@link java.util.SortedSet} of all elements.
     * @throws java.lang.ClassCastException if T does not implement {@link Comparable}
     *
     * The operation is _terminal_.
     */
    @SuppressWarnings("unchecked")
    default Set<T> toSortedSet() {
        return (Set<T>) SequencesKt.toSortedSet((Sequence<Comparable<Object>>) this);
    }

    /**
     * @return a new {@link java.util.SortedSet} of all elements.
     *
     * Elements in the set returned are sorted according to the given {@param comparator}.
     *
     * The operation is _terminal_.
     */
    default Set<T> toSortedSet(Comparator<T> comparator) {
        return SequencesKt.toSortedSet(this, comparator);
    }

    /**
     * @return  a {@link Pair} of lists, where
     * *first* list is built from the first values of each pair from this sequence,
     * *second* list is built from the second values of each pair from this sequence.
     * @throws java.lang.ClassCastException if T is not a {@link Pair}
     *
     * The operation is _terminal_.
     */
    @SuppressWarnings("unchecked")
    default <U,R> Pair<List<U>,List<R>> unzip() {
        return SequencesKt.unzip((Sequence<Pair<U,R>>)this);
    }

    /**
     * @return a sequence of snapshots of the window of the given {@param size}
     * sliding along this sequence with the given {@param step}, where each
     * snapshot is a list.
     *
     * Several last lists may have less elements than the given {@param size}.
     *
     * Both {@param size} and {@param step} must be positive and can be greater than the number of elements in this sequence.
     * @param size the number of elements to take in each window
     * @param step the number of elements to move the window forward by on an each step
     * @param partialWindows controls whether or not to keep partial windows in the end if any
     *
     */
    default Sek<List<T>> windowed(int size, int step, boolean partialWindows) {
        return SequencesKt.windowed(this,size, step, partialWindows)::iterator;
    }

    /**
     * @return a sequence of results of applying the given {@param transform} function to
     * an each list representing a view over the window of the given {@param size}
     * sliding along this sequence with the given {@param step}.
     *
     * Note that the list passed to the {@param transform} function is ephemeral and is valid only inside that function.
     * You should not store it or allow it to escape in some way, unless you made a snapshot of it.
     * Several last lists may have less elements than the given {@param size}.
     *
     * Both {@param size} and {@param step} must be positive and can be greater than the number of elements in this sequence.
     * @param size the number of elements to take in each window
     * @param step the number of elements to move the window forward by on an each step
     * @param partialWindows controls whether or not to keep partial windows in the end if any
     *
     */
    default <R> Sek<R> windowed(int size, int step, boolean partialWindows, Function<? super List<? extends T>, ? extends R> transform) {
        return SequencesKt.<T,R>windowed(this, size, step, partialWindows, transform::apply)::iterator;
    }

    /**
     * @return a sequence that wraps each element of the original sequence
     * into an {@link IndexedValue} containing the index of that element and the element itself.
     *
     * The operation is _intermediate_ and _stateless_.
     */
    default Sek<IndexedValue<T>> withIndex() {
        return SequencesKt.withIndex(this)::iterator;
    }

    /**
     * @return a sequence of values built from the elements of `this` sequence and the {@param other} sequence with the same index.
     * The resulting sequence ends as soon as the shortest input sequence ends.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<Pair<T,R>> zip(Sequence<R> other) {
        return SequencesKt.zip(this, other)::iterator;
    }

    /**
     * @return a sequence of values built from the elements of `this` sequence and the {@param other} sequence with the same index
     * using the provided {@param zipper} function applied to each pair of elements.
     * The resulting sequence ends as soon as the shortest input sequence ends.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <U, R> Sek<R> zip(Sequence<U> other, BiFunction<? super T, ? super U, R> zipper) {
        return SequencesKt.zip(this, other, zipper::apply)::iterator;
    }

    /**
     * @return a sequence of pairs of each two adjacent elements in this sequence.
     *
     * The returned sequence is empty if this sequence contains less than two elements.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default Sek<Pair<T,T>> zipWithNext() {
        return SequencesKt.zipWithNext(this)::iterator;
    }

    /**
     * @return a sequence containing the results of applying the given {@param transform} function
     * to an each pair of two adjacent elements in this sequence.
     *
     * The returned sequence is empty if this sequence contains less than two elements.
     *
     * The operation is _intermediate_ and _stateless_.
     *
     */
    default <R> Sek<R> zipWithNext(BiFunction<? super T, ? super T, ? extends R> transform) {
        return SequencesKt.<T, R>zipWithNext(this, transform::apply)::iterator;
    }
}
