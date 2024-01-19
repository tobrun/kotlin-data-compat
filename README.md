## Kotlin-data-compat

### Context
The Data-compat library resolves the Java binary incompatibility that library developers face when using Kotlin data classes. In a nutshell, every change to a data class results in major breaking change. This project attempts to resolve this binary incompatibility by using annotation processing. Users can keep using their original data classes definitions, but the generated code will compatible for Java consumption.
To read more about this incompatibility, please refer to Jake Wharton's [Public API challenges in Kotlin](https://jakewharton.com/public-api-challenges-in-kotlin/) blogpost.

This library uses [Kotlin Symbol Processing API (KSP)](https://kotlinlang.org/docs/ksp-overview.html) in combination with [KotlinPoet](https://square.github.io/kotlinpoet/) to generate Kotlin classes. Input is a private data class that supports a `@DataCompat` annotation and the code generator outputs a Kotlin class that supports a builder pattern compatible for Java usage.

### Add to your project
The project is hosted on [jitpack](https://jitpack.io/) and requires to add jitpack.io lookup to your gradle configuration:

```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

Since this project uses ksp, you will have to include it:
```groovy
plugins {
  id 'com.google.devtools.ksp' version '1.6.10-1.0.2' apply false
}
```

And you will have to include the required dependencies:

```groovy
dependencies {
  implementation 'com.github.tobrun.kotlin-data-compat:annotation:0.8.0'
  ksp 'com.github.tobrun.kotlin-data-compat:processor:0.8.0'
}
```


### Getting started

Given an existing data class:
 - add `@DataCompat` annotation
 - mark class private
 - append `Data` to the class name
 - support default parameters by using `@Default` annotation
 - support imports for default parameters
 - retain existing class annotations (but not parameters for them)
 - retain existing interfaces
 - make non-nullable parameters without defaults mandatory Builder constructor parameters

For example:

```kotlin
import com.tobrun.datacompat.annotation.DataCompat
import com.tobrun.datacompat.annotation.Default

interface SampleInterface
annotation class SampleAnnotation

/**
 * Represents a person.
 * @property name The full name.
 * @property nickname The nickname.
 * @property age The age.
 */
@DataCompat(importsForDefaults = ["java.util.Date"])
@SampleAnnotation
private data class PersonData(
    @Default("\"John\" + Date(1580897313933L).toString()")
    val name: String,
    /**
     * Additional comment.
     */
    val nickname: String?,
    @Default("42")
    val age: Int,
    @Default("50.2f")
    val euroAmount: Float,
    @Default("300.0")
    val dollarAmount: Double?,
    /**
     * Extra info. Mandatory property.
     */
    val extraInfo: String,
) : SampleInterface
```

After compilation, the following class will be generated:

```kotlin
@file:Suppress("RedundantVisibilityModifier")

package com.tobrun.`data`.compat.example

import java.util.Date
import java.util.Objects
import kotlin.Any
import kotlin.Boolean
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.jvm.JvmSynthetic

/**
 * Represents a person.
 * @property name The full name.
 * @property nickname The nickname.
 * @property age The age.
 */
@SampleAnnotation
public class Person private constructor(
    /**
     * Name.
     */
    public val name: String,
    /**
     * Additional comment.
     */
    public val nickname: String?,
    /**
     * Age.
     */
    public val age: Int,
    /**
     * Euro amount.
     */
    public val euroAmount: Float,
    /**
     * Dollar amount.
     */
    public val dollarAmount: Double?,
    /**
     * Extra info. Mandatory property.
     */
    public val extraInfo: String
) : SampleInterface {
    /**
     * Overloaded toString function.
     */
    public override fun toString() = """Person(name=$name, nickname=$nickname, age=$age,
      euroAmount=$euroAmount, dollarAmount=$dollarAmount, extraInfo=$extraInfo)""".trimIndent()

    /**
     * Overloaded equals function.
     */
    public override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Person
        return name == other.name && nickname == other.nickname && age == other.age &&
                euroAmount.compareTo(other.euroAmount) == 0 &&
                (dollarAmount ?: 0.0).compareTo(other.dollarAmount ?: 0.0) == 0 &&
                extraInfo == other.extraInfo
    }

    /**
     * Overloaded hashCode function based on all class properties.
     */
    public override fun hashCode(): Int = Objects.hash(name, nickname, age, euroAmount, dollarAmount,
        extraInfo)

    /**
     * Convert to Builder allowing to change class properties.
     */
    public fun toBuilder(): Builder = Builder(extraInfo) .setName(name) .setNickname(nickname)
        .setAge(age) .setEuroAmount(euroAmount) .setDollarAmount(dollarAmount)
        .setExtraInfo(extraInfo)

    /**
     * Composes and builds a [Person] object.
     *
     * This is a concrete implementation of the builder design pattern.
     */
    public class Builder(
        /**
         * Extra info. Mandatory property.
         */
        @set:JvmSynthetic
        public var extraInfo: String
    ) {
        /**
         * Name.
         */
        @set:JvmSynthetic
        public var name: String = "John" + Date(1580897313933L).toString()

        /**
         * Additional comment.
         */
        @set:JvmSynthetic
        public var nickname: String? = null

        /**
         * Age.
         */
        @set:JvmSynthetic
        public var age: Int = 42

        /**
         * Euro amount.
         */
        @set:JvmSynthetic
        public var euroAmount: Float = 50.2f

        /**
         * Dollar amount.
         */
        @set:JvmSynthetic
        public var dollarAmount: Double? = 300.0

        /**
         * Setter for name: name.
         *
         * @param name
         * @return Builder
         */
        public fun setName(name: String): Builder {
            this.name = name
            return this
        }

        /**
         * Setter for nickname: additional comment.
         *
         * @param nickname
         * @return Builder
         */
        public fun setNickname(nickname: String?): Builder {
            this.nickname = nickname
            return this
        }

        /**
         * Setter for age: age.
         *
         * @param age
         * @return Builder
         */
        public fun setAge(age: Int): Builder {
            this.age = age
            return this
        }

        /**
         * Setter for euroAmount: euro amount.
         *
         * @param euroAmount
         * @return Builder
         */
        public fun setEuroAmount(euroAmount: Float): Builder {
            this.euroAmount = euroAmount
            return this
        }

        /**
         * Setter for dollarAmount: dollar amount.
         *
         * @param dollarAmount
         * @return Builder
         */
        public fun setDollarAmount(dollarAmount: Double?): Builder {
            this.dollarAmount = dollarAmount
            return this
        }

        /**
         * Setter for extraInfo: extra info. Mandatory property.
         *
         * @param extraInfo
         * @return Builder
         */
        public fun setExtraInfo(extraInfo: String): Builder {
            this.extraInfo = extraInfo
            return this
        }

        /**
         * Returns a [Person] reference to the object being constructed by the builder.
         *
         * @return Person
         */
        public fun build(): Person = Person(name, nickname, age, euroAmount, dollarAmount, extraInfo)
    }
}

/**
 * Creates a [Person] through a DSL-style builder.
 *
 * @param initializer the initialisation block
 * @return Person
 */
@JvmSynthetic
public fun Person(extraInfo: String, initializer: Person.Builder.() -> Unit): Person =
    Person.Builder(extraInfo).apply(initializer).build()
```
