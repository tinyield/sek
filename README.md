# Sek
Sek is a **Java** wrapper for **Kotlin's** _Sequence_. It allows the use of **Kotlin's** _Sequence_ operations in **Java** without losing pipeline fluency.

## Installation
This project can be installed into yours by adding a maven dependency, like so:
```xml
<dependency>
    <groupId>org.tinyield</groupId>
    <artifactId>sek</artifactId>
    <version>1.0.0</version>
</dependency>
```

If you would prefer not to add a dependency to this project, seeing as it is rather small, you can also just copy the [Sek.java](/src/main/java/org/tinyield/Sek.java) file to your project. You will however need to add **Kotlin** to your project, so if you're using maven:
```xml
<dependency>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-stdlib</artifactId>
    <version>${kotlin.version}</version>
</dependency>
<dependency>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-maven-plugin</artifactId>
    <version>${kotlin.version}</version>
</dependency>
```

For more information check **Kotlin's** official page on using maven [here](https://kotlinlang.org/docs/reference/using-maven.html).

## Usage
Using Sek is quite simple, you have create a Sek using one of three methods _of_, _generate_ or _empty_ and then chain operations into the pipeline until you call a terminal operation like _forEach_ or _reduce_. For the full list of supported methods check [Sek.java](/src/main/java/org/tinyield/Sek.java).

```java
String result = Sek.of(1,2,3)
                    .map(String::valueOf)
                    .reduce(String::concat);

System.out.println(result);

// Output
// 123
```
## License

This project is licensed under [Apache License,
version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
