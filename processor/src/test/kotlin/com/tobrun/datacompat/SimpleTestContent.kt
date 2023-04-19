package com.tobrun.datacompat

internal val expectedSimpleTestContent = """
@file:Suppress("RedundantVisibilityModifier")

import java.util.Date
import java.util.Objects
import kotlin.Any
import kotlin.Boolean
import kotlin.Deprecated
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.jvm.JvmSynthetic

/**
 * Represents a person.
 * @property name The full name.
 * @property nickname The nickname.
 * @property age The age.
 * @property veryLongAndVeryDetailedDescription The very long and very detailed description.
 */
@Deprecated
public class Person private constructor(
  /**
   * Name.
   */
  public val name: String,
  /**
   * Nickname.
   */
  public val nickname: String?,
  /**
   * Age.
   */
  public val age: Int,
  /**
   * Actually it's a very short description.
   */
  public val veryLongAndVeryDetailedDescription: String?,
  /**
   * Parameter that will become constructor parameter.
   */
  public val mandatoryDoubleWithoutDefault: Double
) : EmptyInterface, EmptyInterface2 {
  /**
   * Overloaded toString function.
   */
  public override fun toString() = ""${"\""}Person(name=%name, nickname=%nickname, age=%age,
      veryLongAndVeryDetailedDescription=%veryLongAndVeryDetailedDescription,
      mandatoryDoubleWithoutDefault=%mandatoryDoubleWithoutDefault)""${"\""}.trimIndent()

  /**
   * Overloaded equals function.
   */
  public override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as Person
    return name == other.name && nickname == other.nickname && age == other.age &&
        veryLongAndVeryDetailedDescription == other.veryLongAndVeryDetailedDescription &&
        mandatoryDoubleWithoutDefault.compareTo(other.mandatoryDoubleWithoutDefault) == 0
  }

  /**
   * Overloaded hashCode function based on all class properties.
   */
  public override fun hashCode(): Int = Objects.hash(name, nickname, age,
      veryLongAndVeryDetailedDescription, mandatoryDoubleWithoutDefault)

  /**
   * Convert to Builder allowing to change class properties.
   */
  public fun toBuilder(): Builder = Builder(mandatoryDoubleWithoutDefault) .setName(name)
      .setNickname(nickname) .setAge(age)
      .setVeryLongAndVeryDetailedDescription(veryLongAndVeryDetailedDescription)
      .setMandatoryDoubleWithoutDefault(mandatoryDoubleWithoutDefault)

  /**
   * Composes and builds a [Person] object.
   *
   * This is a concrete implementation of the builder design pattern.
   */
  public class Builder(
    /**
     * Parameter that will become constructor parameter.
     */
    @set:JvmSynthetic
    public var mandatoryDoubleWithoutDefault: Double
  ) {
    /**
     * Name.
     */
    @set:JvmSynthetic
    public var name: String = "John"

    /**
     * Nickname.
     */
    @set:JvmSynthetic
    public var nickname: String? = null

    /**
     * Age.
     */
    @set:JvmSynthetic
    public var age: Int = 23

    /**
     * Actually it's a very short description.
     */
    @set:JvmSynthetic
    public var veryLongAndVeryDetailedDescription: String? = null

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
     * Setter for nickname: nickname.
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
     * Setter for veryLongAndVeryDetailedDescription: actually it's a very short description.
     *
     * @param veryLongAndVeryDetailedDescription
     * @return Builder
     */
    public fun setVeryLongAndVeryDetailedDescription(veryLongAndVeryDetailedDescription: String?):
        Builder {
      this.veryLongAndVeryDetailedDescription = veryLongAndVeryDetailedDescription
      return this
    }

    /**
     * Setter for mandatoryDoubleWithoutDefault: parameter that will become constructor parameter.
     *
     * @param mandatoryDoubleWithoutDefault
     * @return Builder
     */
    public fun setMandatoryDoubleWithoutDefault(mandatoryDoubleWithoutDefault: Double): Builder {
      this.mandatoryDoubleWithoutDefault = mandatoryDoubleWithoutDefault
      return this
    }

    /**
     * Returns a [Person] reference to the object being constructed by the builder.
     *
     * @return Person
     */
    public fun build(): Person = Person(name, nickname, age, veryLongAndVeryDetailedDescription,
        mandatoryDoubleWithoutDefault)
  }
}

/**
 * Creates a [Person] through a DSL-style builder.
 *
 * @param initializer the initialisation block
 * @return Person
 */
@JvmSynthetic
public fun Person(mandatoryDoubleWithoutDefault: Double, initializer: Person.Builder.() -> Unit):
    Person = Person.Builder(mandatoryDoubleWithoutDefault).apply(initializer).build()

""".trimIndent().replace('%', '$')
