# Sek
_Sek_ is a **Java** wrapper for **Kotlin's**
[_Sequence_](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence.html).

With _Sek_ you can use the full suite of operations that **Kotlin's**
[_Sequence_](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence.html) provide without leaving the
**Java** ecosystem, on top of outperforming **Java's** _Stream_ in sequential processing operations in a variety of
use-cases.

## Installation
This project can be installed into yours by adding a maven dependency, like so:
```xml
<dependency>
    <groupId>org.tinyield</groupId>
    <artifactId>sek</artifactId>
    <version>1.0.0</version>
</dependency>
```

If you would prefer not to add a dependency to this project, you can also just copy the
[Sek.java](/src/main/java/org/tinyield/Sek.java) file to your project. You will however need to add **Kotlin** to your
project's dependencies, so if you're using maven:
```xml
<dependency>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-stdlib</artifactId>
    <version>${kotlin.version}</version>
</dependency>
```

For more information check **Kotlin's** official page on using maven [here](https://kotlinlang.org/docs/reference/using-maven.html).

## Usage
Create a _Sek_ using _[of](https://github.com/tinyield/sek/blob/master/src/main/java/org/tinyield/Sek.java#L42)_,
_[generate](https://github.com/tinyield/sek/blob/master/src/main/java/org/tinyield/Sek.java#L66)_ or the
_[empty](https://github.com/tinyield/sek/blob/master/src/main/java/org/tinyield/Sek.java#L73)_ method and then chain
operations into the pipeline until you call a terminal operation like
_[forEach](https://github.com/tinyield/sek/blob/master/src/main/java/org/tinyield/Sek.java#L587)_ or
_[reduce](https://github.com/tinyield/sek/blob/master/src/main/java/org/tinyield/Sek.java#L1088)_. For the full list of
supported methods check [Sek.java](/src/main/java/org/tinyield/Sek.java) or the official Kotlin documentation about
[Sequence](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/).

```java
Sek<String> songs = Sek.of(
        new Song("505", "Alternative"),
        new Song("Amsterdam", "Alternative"),
        new Song("Mural","Hip-Hop")
)
.filterNot(song -> song.name().startsWith("A"))
.map(Song::name);

Sek<String> artists = Sek.of(
    new Artist("Arctic Monkeys", "band"),
    new Artist("Nothing But Thieves", "band"),
    new Artist("Lupe Fiasco", "solo-artist")
)
.distinctBy(Artist::type)
.map(Artist::name);

songs.zip(artists, (song, artist) -> String.format("%s by %s", song, artist))
     .forEach(System.out::println);

// Output
// 505 by Arctic Monkeys
// Mural by Lupe Fiasco
```

You can also add user-defined operations to your pipeline by using the
[_then_](https://github.com/tinyield/sek/blob/master/src/main/java/org/tinyield/Sek.java#L1429) method,
even using **Kotlin's** extension methods. Let's say you have a `Extensions.kt` file with the following definition:
```kotlin
fun <T> Sequence<T>.oddIndexes() = sequence<T> {
    var isOdd = false
    for (item in this@oddIndexes) {
        if(isOdd) yield(item)
        isOdd = !isOdd
    }
}
```
You could use it with _Sek_ like this:
```java
 Sek.of("a", "b", "c", "d", "f", "e")
    .then(Extensionskt::oddIndexes)
    .forEach(out::println)

// Output
// b
// d
// e
```

## License

This project is licensed under [Apache License,
version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
