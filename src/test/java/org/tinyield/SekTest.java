package org.tinyield;


import kotlin.Pair;
import kotlin.collections.Grouping;
import kotlin.collections.IndexedValue;
import kotlin.random.Random;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class SekTest {

    @Test()
    void of() {
        Sek<Integer> input = Sek.of(1, 2, 3);
        List<Integer> expected = SequencesKt.toList(SequencesKt.sequenceOf(1, 2, 3));
        List<Integer> actual = new ArrayList<>(3);

        input.forEachIndexed(actual::add);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void generate() {
        Iterator<Integer> input = Sek.generate(() -> 1).iterator();
        List<Integer> expected = List.of(1, 1, 1);
        List<Integer> actual = new ArrayList<>(3);
        actual.add(input.next());
        actual.add(input.next());
        actual.add(input.next());

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void empty() {
        assertThat(Sek.empty().iterator().hasNext()).isFalse();
    }

    @Test()
    void all() {
        assertThat(Sek.of(1, 2, 3).all(i -> i < 5)).isTrue();
    }

    @Test()
    void any() {
        assertThat(Sek.of(1).any()).isTrue();
    }

    @Test()
    void testAny() {
        assertThat(Sek.of(1, 7, 3).any(i -> i > 5)).isTrue();
    }

    @Test()
    void asIterable() {
        Iterable<Integer> input = Sek.of(1, 2, 3).asIterable();
        List<Integer> expected = SequencesKt.toList(SequencesKt.sequenceOf(1, 2, 3));
        List<Integer> actual = new ArrayList<>(3);

        input.iterator().forEachRemaining(actual::add);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void asSequence() {
        Sequence<Integer> input = Sek.of(1, 2, 3).asSequence();
        List<Integer> expected = SequencesKt.toList(SequencesKt.sequenceOf(1, 2, 3));
        List<Integer> actual = new ArrayList<>(3);

        input.iterator().forEachRemaining(actual::add);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void associate() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 2);
        expected.put(2, 4);
        expected.put(3, 6);
        Map<Integer, Integer> actual = Sek.of(1, 2, 3)
                .associate(i -> new Pair<>(i, i * 2));

        assertThat(actual).isEqualTo(expected);
    }

    @Test()
    void associateBy() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2, 1);
        expected.put(4, 2);
        expected.put(6, 3);
        Map<Integer, Integer> actual = Sek.of(1, 2, 3)
                .associateBy(i -> i * 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test()
    void testAssociateBy() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 2);
        expected.put(2, 4);
        expected.put(3, 6);
        Map<Integer, Integer> actual = Sek.of(1, 2, 3)
                .associateBy(i -> i, i -> i * 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test()
    void associateByTo() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(2, 1);
        expected.put(4, 2);
        expected.put(6, 3);
        Map<Integer, Integer> actual = Sek.of(1, 2, 3)
                .associateByTo(new HashMap<>(), i -> i * 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test()
    void testAssociateByTo() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 2);
        expected.put(2, 4);
        expected.put(3, 6);
        Map<Integer, Integer> actual = Sek.of(1, 2, 3)
                .associateByTo(new HashMap<>(), i -> i, i -> i * 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test()
    void associateTo() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 2);
        expected.put(2, 4);
        expected.put(3, 6);
        Map<Integer, Integer> actual = Sek.of(1, 2, 3)
                .associateTo(new HashMap<>(), i -> new Pair<>(i, i * 2));

        assertThat(actual).isEqualTo(expected);
    }

    @Test()
    void associateWith() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 2);
        expected.put(2, 4);
        expected.put(3, 6);
        Map<Integer, Integer> actual = Sek.of(1, 2, 3)
                .associateWith(i -> i * 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test()
    void associateWithTo() {
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 2);
        expected.put(2, 4);
        expected.put(3, 6);
        Map<Integer, Integer> actual = Sek.of(1, 2, 3)
                .associateWithTo(new HashMap<>(), i -> i * 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test()
    void chunked() {
        List<List<Integer>> expected = List.of(List.of(1), List.of(2), List.of(3));
        List<List<Integer>> actual = new ArrayList<>();
        Sek.of(1, 2, 3)
                .chunked(1)
                .forEach(actual::add);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void testChunked() {
        List<Integer> expected = List.of(2, 4, 6);
        List<Integer> actual = new ArrayList<>();
        Sek.of(1, 2, 3)
                .chunked(1, l -> l.get(0) * 2)
                .iterator()
                .forEachRemaining(actual::add);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    void constrainOnce() {
        Sek<Integer> input = Sek.of(1, 2, 3).constrainOnce();
        List<Integer> actual = new ArrayList<>();
        input.forEach(actual::add);
        input.forEach(actual::add);
        assertThat(actual.size()).isEqualTo(3);
    }

    @Test()
    void contains() {
        assertThat(Sek.of(1, 2, 3).contains(2)).isTrue();
    }

    @Test()
    void count() {
        assertThat(Sek.of(1, 2, 3).count()).isEqualTo(3);
    }

    @Test()
    void testCount() {
        assertThat(Sek.of(1, 2, 3).count(i -> i >= 2)).isEqualTo(2);
    }

    @Test()
    void distinct() {
        Set<Integer> expected = Set.of(1, 2, 3);
        Set<Integer> actual = Sek.of(1, 2, 3, 1, 2, 3)
                .distinct()
                .toSortedSet(Integer::compareTo);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void distinctBy() {
        List<Integer> expected = List.of(1, 2, 3);
        List<Integer> actual = Sek.of(1, 2, 3, 1, 2, 3)
                .distinctBy(i -> i * 2)
                .toMutableList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void drop() {
        List<Integer> expected = List.of(2, 3);
        List<Integer> actual = Sek.of(1, 2, 3)
                .drop(1)
                .toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void dropWhile() {
        List<Integer> expected = List.of(3, 2);
        List<Integer> actual = Sek.of(1, 2, 3, 2)
                .dropWhile(i -> i <= 2)
                .toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void elementAt() {
        assertThat(Sek.of(1, 2, 3).elementAt(0)).isOne();
    }

    @Test()
    void elementAtOrElse() {
        assertThat(Sek.of(1, 2, 3).elementAtOrElse(3, i -> 4)).isEqualTo(4);
    }

    @Test()
    void elementAtOrNull() {
        assertThat(Sek.of(1, 2, 3).elementAtOrNull(3)).isNull();
    }

    @Test()
    void filter() {
        List<Integer> expected = List.of(1, 2);
        List<Integer> actual = Sek.of(1, 2, 3)
                .filter(i -> i <= 2)
                .toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void filterTo() {
        List<Integer> expected = List.of(1, 2);
        List<Integer> actual = Sek.of(1, 2, 3)
                .filterTo(new ArrayList<>(), i -> i <= 2);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void filterIndexed() {
        Collection<Integer> expected = List.of(1);
        Collection<Integer> actual = Sek.of(1, 2, 3)
                .filterIndexed((idx, val) -> idx * val < 2)
                .toCollection(new ArrayList<>());

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void filterIndexedTo() {
        List<Integer> expected = List.of(1);
        List<Integer> actual = Sek.of(1, 2, 3)
                .filterIndexedTo(new ArrayList<>(), (idx, val) -> idx * val < 2);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void filterIsInstance() {
        Set<String> expected = Set.of("2");
        Set<String> actual = Sek.of(null, 1, 2, "2")
                .filterIsInstance(String.class)
                .toHashSet();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void filterIsInstanceTo() {
        List<String> expected = List.of("2");
        List<String> actual = Sek.of(null, 1, 2, "2")
                .filterIsInstanceTo(new ArrayList<>(), String.class);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void filterNot() {
        Set<Integer> expected = Set.of(3);
        Set<Integer> actual = Sek.of(1, 2, 3)
                .filterNot(i -> i <= 2)
                .toMutableSet();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void filterNotTo() {
        List<Integer> expected = List.of(3);
        List<Integer> actual = Sek.of(1, 2, 3)
                .filterNotTo(new ArrayList<>(), i -> i <= 2);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void filterNotNull() {
        List<Object> expected = List.of(1, 2, "2");
        List<Object> actual = Sek.<Object>of(null, 1, 2, "2")
                .filterNotNull()
                .toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void filterNotNullTo() {
        List<Object> expected = List.of(1, 2, "2");
        List<Object> actual = Sek.<Object>of(null, 1, 2, "2")
                .filterNotNullTo(new ArrayList<>());

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void first() {
        assertThat(Sek.of(1, 2, 3).first()).isOne();
    }

    @Test()
    void testFirst() {
        assertThat(Sek.of(1, 2, 3).first(i -> i >= 2)).isEqualTo(2);
    }

    @Test()
    void firstOrNull() {
        assertThat(Sek.empty().firstOrNull()).isNull();
    }

    @Test()
    void testFirstOrNull() {
        assertThat(Sek.of(1, 2, 3).firstOrNull(i -> i >= 5)).isNull();
    }

    @Test()
    void flatMap() {
        Set<Integer> expected = Set.of(1, 2, 3);
        Set<Integer> actual = Sek.of(Sek.of(1), Sek.of(2), Sek.of(3))
                .flatMap(i -> i)
                .toSet();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void flatMapTo() {
        List<Integer> expected = List.of(1, 2, 3);
        List<Integer> actual = Sek.of(Sek.of(1), Sek.of(2), Sek.of(3))
                .flatMapTo(new ArrayList<>(), i -> i);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void fold() {
        assertThat(Sek.of(1, 2, 3).fold(0, Integer::sum)).isEqualTo(6);
    }

    @Test()
    void foldIndexed() {
        assertThat(Sek.of(1, 2, 3).foldIndexed(0, (idx, acc, curr) -> idx + acc + curr)).isEqualTo(9);
    }

    @Test()
    void groupBy() {
        Map<String, List<Integer>> expected = new HashMap<>();
        expected.put("lesser", List.of(1, 2));
        expected.put("greaterOrEqual", List.of(3));
        Map<String, List<Integer>> actual = Sek.of(1, 2, 3)
                .groupBy(i -> i < 3 ? "lesser" : "greaterOrEqual");
        Set<String> expectedKeySet = expected.keySet();
        Set<String> actualKeySet = actual.keySet();

        assertThat(actualKeySet).hasSameElementsAs(expectedKeySet);
        for (String key : actualKeySet) {
            assertThat(actual.get(key)).hasSameElementsAs(expected.get(key));
        }
    }

    @Test()
    void groupByTo() {
        Map<String, List<Integer>> expected = new HashMap<>();
        expected.put("lesser", List.of(1, 2));
        expected.put("greaterOrEqual", List.of(3));
        Map<String, List<Integer>> actual = Sek.of(1, 2, 3)
                .groupByTo(new HashMap<>(), i -> i < 3 ? "lesser" : "greaterOrEqual");
        Set<String> expectedKeySet = expected.keySet();
        Set<String> actualKeySet = actual.keySet();

        assertThat(actualKeySet).hasSameElementsAs(expectedKeySet);
        for (String key : actualKeySet) {
            assertThat(actual.get(key)).hasSameElementsAs(expected.get(key));
        }
    }

    @Test()
    void testGroupBy() {
        Map<String, List<Integer>> expected = new HashMap<>();
        expected.put("lesser", List.of(2, 4));
        expected.put("greaterOrEqual", List.of(6));
        Map<String, List<Integer>> actual = Sek.of(1, 2, 3)
                .groupBy(i -> i < 3 ? "lesser" : "greaterOrEqual", i -> i * 2);
        Set<String> expectedKeySet = expected.keySet();
        Set<String> actualKeySet = actual.keySet();

        assertThat(actualKeySet).hasSameElementsAs(expectedKeySet);
        for (String key : actualKeySet) {
            assertThat(actual.get(key)).hasSameElementsAs(expected.get(key));
        }
    }

    @Test()
    void testGroupByTo() {
        Map<String, List<Integer>> expected = new HashMap<>();
        expected.put("lesser", List.of(2, 4));
        expected.put("greaterOrEqual", List.of(6));
        Map<String, List<Integer>> actual = Sek.of(1, 2, 3)
                .groupByTo(new HashMap<>(), i -> i < 3 ? "lesser" : "greaterOrEqual", i -> i * 2);
        Set<String> expectedKeySet = expected.keySet();
        Set<String> actualKeySet = actual.keySet();

        assertThat(actualKeySet).hasSameElementsAs(expectedKeySet);
        for (String key : actualKeySet) {
            assertThat(actual.get(key)).hasSameElementsAs(expected.get(key));
        }
    }

    @Test()
    void groupingBy() {
        Grouping<Integer, String> actual = Sek.of(1, 2, 3)
                .groupingBy(i -> i < 3 ? "lesser" : "greaterOrEqual");

        for (Iterator<Integer> it = actual.sourceIterator(); it.hasNext(); ) {
            Integer value = it.next();
            if (value < 3) {
                assertThat(actual.keyOf(value)).isEqualTo("lesser");
            } else {
                assertThat(actual.keyOf(value)).isEqualTo("greaterOrEqual");
            }
        }

    }

    @Test()
    void ifEmpty() {
        assertThat(Sek.empty().ifEmpty(() -> Sek.of(1)).count()).isOne();
    }

    @Test()
    void indexOf() {
        assertThat(Sek.of(1, 2, 3).indexOf(2)).isOne();
    }

    @Test()
    void indexOfFirst() {
        assertThat(Sek.of(1, 2, 3).indexOfFirst(i -> i < 3)).isZero();
    }

    @Test()
    void indexOfLast() {
        assertThat(Sek.of(1, 2, 3).indexOfLast(i -> i < 3)).isOne();
    }

    @Test()
    void joinTo() {
        String actual = Sek.of(1, 2, 3)
                .joinTo(
                        new StringBuilder(),
                        "-",
                        "_",
                        "?",
                        2,
                        "",
                        i -> i + "" + i
                )
                .toString();
        assertThat(actual).isEqualTo("_11-22-?");
    }

    @Test()
    void joinToString() {
        String actual = Sek.of(1, 2, 3)
                .joinToString(
                        "-",
                        "_",
                        "?",
                        2,
                        "",
                        i -> i + "" + i
                );
        assertThat(actual).isEqualTo("_11-22-?");
    }

    @Test()
    void last() {
        assertThat(Sek.of(1, 2, 3).last()).isEqualTo(3);
    }

    @Test()
    void testLast() {
        assertThat(Sek.of(1, 2, 3).last(i -> i < 3)).isEqualTo(2);
    }

    @Test()
    void lastIndexOf() {
        assertThat(Sek.of(1, 2, 3, 2).lastIndexOf(2)).isEqualTo(3);
    }

    @Test()
    void lastOrNull() {
        assertThat(Sek.empty().lastOrNull()).isNull();
    }

    @Test()
    void testLastOrNull() {
        assertThat(Sek.of(1, 2, 3).lastOrNull(i -> i > 3)).isNull();
    }

    @Test()
    void map() {
        Set<String> expected = Set.of("1", "2", "3");
        Set<String> actual = Sek.of(1, 2, 3)
                .map(String::valueOf)
                .toHashSet();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void mapTo() {
        Set<String> expected = Set.of("1", "2", "3");
        Set<String> actual = Sek.of(1, 2, 3)
                .mapTo(new HashSet<>(), String::valueOf);

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void mapIndexed() {
        Set<String> expected = Set.of("0", "2", "6");
        Set<String> actual = Sek.of(1, 2, 3)
                .mapIndexed((index, value) -> String.valueOf(index * value))
                .toHashSet();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void mapIndexedTo() {
        Set<String> expected = Set.of("0", "2", "6");
        Set<String> actual = Sek.of(1, 2, 3)
                .mapIndexedTo(new HashSet<>(), (index, value) -> String.valueOf(index * value));

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void mapIndexedNotNull() {
        Set<String> expected = Set.of("0", "4", "12");
        Set<String> actual = Sek.of(1, null, 2, null, 3)
                .mapIndexedNotNull((index, value) -> value == null ? null :  String.valueOf(index * value))
                .toHashSet();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void mapIndexedNotNullTo() {
        Set<String> expected = Set.of("0", "4", "12");
        Set<String> actual = Sek.of(1, null, 2, null, 3)
                .mapIndexedNotNullTo(new HashSet<>(), (index, value) -> value == null ? null : String.valueOf(index * value));

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void mapNotNull() {
        Set<String> expected = Set.of("1", "2", "3");
        Set<String> actual = Sek.of(1, null, 2, null, 3)
                .mapNotNull(i -> i == null ? null : String.valueOf(i))
                .toHashSet();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void mapNotNullTo() {
        Set<String> expected = Set.of("1", "2", "3");
        Set<String> actual = Sek.of(1, null, 2, null, 3)
                .mapNotNullTo(new HashSet<>(), i -> i == null ? null : String.valueOf(i));

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void maxByOrNull() {
        assertThat(Sek.of(1,2,3).maxByOrNull(i -> i)).isEqualTo(3);
        assertThat(Sek.<Integer>empty().maxByOrNull(i -> i)).isNull();
    }

    @Test()
    void maxWithOrNull() {
        assertThat(Sek.of(1,2,3).maxWithOrNull(Integer::compare)).isEqualTo(3);
        assertThat(Sek.<Integer>empty().maxWithOrNull(Integer::compare)).isNull();
    }

    @Test()
    void minByOrNull() {
        assertThat(Sek.of(1,2,3).minByOrNull(i -> i)).isEqualTo(1);
        assertThat(Sek.<Integer>empty().minByOrNull(i -> i)).isNull();
    }

    @Test()
    void minus() {
        assertThat(Sek.of(1,2,3).minus(3).count()).isEqualTo(2);
    }

    @Test()
    void testMinus() {
        assertThat(Sek.of(1,2,3).minus(new Integer[]{1,2}).count()).isOne();
    }

    @Test()
    void testMinus1() {
        assertThat(Sek.of(1,2,3).minus(List.of(1,2)).count()).isOne();
    }

    @Test()
    void testMinus2() {
        assertThat(Sek.of(1,2,3).minus(Sek.of(1,2)).count()).isOne();
    }

    @Test()
    void minWithOrNull() {
        assertThat(Sek.of(1,2,3).minWithOrNull(Integer::compare)).isEqualTo(1);
        assertThat(Sek.<Integer>empty().minWithOrNull(Integer::compare)).isNull();
    }

    @Test()
    void none() {
        assertThat(Sek.of(1, 2, 3).filter(i -> i > 5).none()).isTrue();
    }

    @Test()
    void testNone() {
        assertThat(Sek.of(1, 2, 3).none(i -> i > 5)).isTrue();
    }

    @Test()
    void onEach() {
        Set<Integer> expectedEach = Set.of(2,4,6);
        Set<Integer> expected = Set.of(1,2,3);
        Set<Integer> actualEach = new HashSet<>();
        Set<Integer> actual = Sek.of(1, 2, 3)
                .onEach(i -> actualEach.add(i * 2))
                .toHashSet();

        assertThat(actual).hasSameElementsAs(expected);
        assertThat(actualEach).hasSameElementsAs(expectedEach);
    }

    @Test()
    void onEachIndexed() {
        Set<Integer> expectedEach = Set.of(0,2,6);
        Set<Integer> expected = Set.of(1,2,3);
        Set<Integer> actualEach = new HashSet<>();
        Set<Integer> actual = Sek.of(1, 2, 3)
                .onEachIndexed((idx, val) -> actualEach.add(idx * val))
                .toHashSet();

        assertThat(actual).hasSameElementsAs(expected);
        assertThat(actualEach).hasSameElementsAs(expectedEach);
    }

    @Test()
    void partition() {
        Pair<List<Integer>, List<Integer>> expected = new Pair<>(List.of(1,2), List.of(3));
        Pair<List<Integer>, List<Integer>> actual = Sek.of(1, 2, 3)
                .partition(i -> i < 3);

        assertThat(actual.component1()).hasSameElementsAs(expected.component1());
        assertThat(actual.component2()).hasSameElementsAs(expected.component2());
    }

    @Test()
    void plus() {
        assertThat(Sek.of(1,2,3).plus(3).count()).isEqualTo(4);
    }

    @Test()
    void testPlus() {
        assertThat(Sek.of(1,2,3).plus(new Integer[]{1,2}).count()).isEqualTo(5);
    }

    @Test()
    void testPlus1() {
        assertThat(Sek.of(1,2,3).plus(List.of(1,2)).count()).isEqualTo(5);
    }

    @Test()
    void testPlus2() {
        assertThat(Sek.of(1,2,3).plus(Sek.of(1,2)).count()).isEqualTo(5);
    }

    @Test()
    void reduce() {
        assertThat(Sek.of("a","b","c").reduce(String::concat)).isEqualTo("abc");
    }

    @Test()
    void reduceIndexed() {
        assertThat(Sek.of("a","b","c").reduceIndexed((idx, acc, curr) -> acc.concat(curr + idx))).isEqualTo("ab1c2");
    }

    @Test()
    void reduceIndexedOrNull() {
        assertThat(Sek.of("a","b","c").reduceIndexedOrNull((idx, acc, curr) -> acc.concat(curr + idx))).isEqualTo("ab1c2");
        assertThat(Sek.<String>empty().reduceIndexedOrNull((idx, acc, curr) -> acc.concat(curr + idx))).isNull();
    }

    @Test()
    void reduceOrNull() {
        assertThat(Sek.of("a","b","c").reduceOrNull(String::concat)).isEqualTo("abc");
        assertThat(Sek.<String>empty().reduceOrNull(String::concat)).isNull();
    }

    @Test(expectedExceptions = {IllegalArgumentException.class})
    void requireNoNulls() {
        assertThat(Sek.of("a","b","c").requireNoNulls().count()).isEqualTo(3);
        Sek.of(1, null).requireNoNulls().count();
    }

    @Test()
    void runningFold() {
        assertThat(Sek.of(1, 2, 3).runningFold(0, Integer::sum).count()).isEqualTo(4);
    }

    @Test()
    void runningFoldIndexed() {
        assertThat(Sek.of(1, 2, 3).runningFoldIndexed(0, (idx, acc, curr) -> idx + acc + curr).count()).isEqualTo(4);
        assertThat(Sek.of(1, 2, 3).runningFoldIndexed(0, (idx, acc, curr) -> idx + acc + curr).count()).isEqualTo(4);
    }

    @Test()
    void runningReduce() {
        assertThat(Sek.of("a","b","c").runningReduce(String::concat).count()).isEqualTo(3);
    }

    @Test()
    void runningReduceIndexed() {
        assertThat(Sek.of("a","b","c").runningReduceIndexed((idx, acc, curr) -> acc.concat(curr + idx)).count()).isEqualTo(3);
    }

    @Test()
    void scan() {
        List<String> actual = Sek.of("a", "b", "c")
                .scan("_", String::concat)
                .toList();

        assertThat(actual.size()).isEqualTo(4);
        assertThat(actual.get(0)).isEqualTo("_");
        assertThat(actual.get(3)).isEqualTo("_abc");
    }

    @Test()
    void scanIndexed() {
        List<String> actual = Sek.of("a", "b", "c")
                .scanIndexed("_", (idx, acc, curr) -> acc.concat(curr + idx))
                .toList();

        assertThat(actual.size()).isEqualTo(4);
        assertThat(actual.get(0)).isEqualTo("_");
        assertThat(actual.get(3)).isEqualTo("_a0b1c2");
    }

    @Test()
    void shuffled() {
        List<Integer> expected = List.of(1, 2, 3);
        List<Integer> actual = Sek.of(1,2,3).shuffled().toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void testShuffled() {
        List<Integer> expected = List.of(1, 2, 3);
        List<Integer> actual = Sek.of(1,2,3).shuffled(Random.Default).toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    void single() {
        assertThat(Sek.of(1).single()).isOne();
        Sek.of(1, 2).single();
    }

    @Test(expectedExceptions = java.util.NoSuchElementException.class)
    void testSingle() {
        assertThat(Sek.of(1, 2, 3).single(i -> i == 2)).isEqualTo(2);
        Sek.empty().single();
    }

    @Test()
    void singleOrNull() {
        assertThat(Sek.of(1).singleOrNull()).isOne();
        assertThat(Sek.empty().singleOrNull()).isNull();
    }

    @Test()
    void testSingleOrNull() {
        assertThat(Sek.of(1, 2, 3).singleOrNull(i -> i == 2)).isEqualTo(2);
        assertThat(Sek.of(1, 2, 3).singleOrNull(i -> i == 4)).isNull();
    }

    @Test()
    void sortedBy() {
        List<Integer> expected = List.of(1, 1, 2, 2, 3, 3);
        List<Integer> actual = Sek.of(1, 2, 3, 3, 2, 1)
                .sortedBy(i -> i)
                .toList();
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test()
    void sortedByDescending() {
        List<Integer> expected = List.of(3, 3, 2, 2, 1, 1);
        List<Integer> actual = Sek.of(1, 2, 3, 3, 2, 1)
                .sortedByDescending(i -> i)
                .toList();
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test()
    void sortedWith() {
        List<Integer> expected = List.of(1, 1, 2, 2, 3, 3);
        List<Integer> actual = Sek.of(1, 2, 3, 3, 2, 1)
                .sortedWith(Integer::compare)
                .toList();
        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isEqualTo(expected.get(i));
        }
    }

    @Test()
    void sumBy() {
        assertThat(Sek.of(1, 2, 3).sumBy(Integer::valueOf)).isEqualTo(6);
    }

    @Test()
    void sumByDouble() {
        assertThat(Sek.of(1, 2, 3).sumByDouble(Double::valueOf)).isEqualTo(6D);
    }

    @Test()
    void take() {
        assertThat(Sek.of(1, 2, 3).take(1).count()).isOne();
    }

    @Test()
    void takeWhile() {
        assertThat(Sek.of(1, 2, 3, 1).takeWhile(i -> i < 2).count()).isOne();
    }

    @Test()
    void windowed() {
        List<List<Integer>> expected = List.of(List.of(1,2),List.of(2,3), List.of(3));
        List<List<Integer>> actual = Sek.of(1,2,3)
                .windowed(2,1,true)
                .toList();

        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).hasSameElementsAs(expected.get(i));
        }
    }

    @Test()
    void testWindowed() {
        List<List<Integer>> expected = List.of(List.of(1,2),List.of(2,3), List.of(3));
        List<List<Integer>> actual = Sek.of(1,2,3)
                .windowed(2,1,true, List::<Integer>copyOf)
                .toList();

        assertThat(actual.size()).isEqualTo(expected.size());
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).hasSameElementsAs(expected.get(i));
        }
    }

    @Test()
    void withIndex() {
        List<IndexedValue<String>> expected = List.of(
                new IndexedValue<>(0,"a"),
                new IndexedValue<>(1,"b"),
                new IndexedValue<>(2,"c")
        );
        List<IndexedValue<String>> actual = Sek.of("a","b","c")
                .withIndex()
                .toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void zip() {
        List<Pair<Integer, String>> expected = List.of(
                new Pair<Integer, String>(1,"1"),
                new Pair<Integer, String>(2,"2")
        );
        List<Pair<Integer, String>> actual = Sek.of(1,2).zip(Sek.of("1","2")).toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void testZip() {
        List<Integer> expected = List.of(4, 6);
        List<Integer> actual = Sek.of(1, 2).zip(Sek.of(3, 4), Integer::sum).toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void zipWithNext() {
        List<Pair<Integer, Integer>> expected = List.of(
                new Pair<Integer, Integer>(1,2),
                new Pair<Integer, Integer>(2,3),
                new Pair<Integer, Integer>(3,4)
        );
        List<Pair<Integer, Integer>> actual = Sek.of(1,2,3,4).zipWithNext().toList();

        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test()
    void testZipWithNext() {
        List<Integer> expected = List.of( 3, 5, 7);
        List<Integer> actual = Sek.of(1,2,3,4).zipWithNext(Integer::sum).toList();

        assertThat(actual).hasSameElementsAs(expected);
    }
}
