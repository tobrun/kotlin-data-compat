package com.tobrun.datacompat

internal val expectedSimpleTestContent = """
    import java.util.Objects
    import kotlin.Any
    import kotlin.Boolean
    import kotlin.Deprecated
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
    @Deprecated
    public class Person private constructor(
      public val name: String,
      public val nickname: String?,
      public val age: Int
    ) : EmptyInterface {
      public override fun toString() = "Person(name=%name, nickname=%nickname, age=%age)"
    
      public override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Person
        return name == other.name && nickname == other.nickname && age == other.age
      }
    
      public override fun hashCode(): Int = Objects.hash(name, nickname, age)
    
      /**
       * Composes and builds a [Person] object.
       *
       * This is a concrete implementation of the builder design pattern.
       *
       * @property name The full name.
       * @property nickname The nickname.
       * @property age The age.
       */
      public class Builder {
        @set:JvmSynthetic
        public var name: String? = null
    
        @set:JvmSynthetic
        public var nickname: String? = null
    
        @set:JvmSynthetic
        public var age: Int? = null
    
        /**
         * Set the full name.
         *
         * @param name the full name.
         * @return Builder
         */
        public fun setName(name: String?): Builder {
          this.name = name
          return this
        }
    
        /**
         * Set the nickname.
         *
         * @param nickname the nickname.
         * @return Builder
         */
        public fun setNickname(nickname: String?): Builder {
          this.nickname = nickname
          return this
        }
    
        /**
         * Set the age.
         *
         * @param age the age.
         * @return Builder
         */
        public fun setAge(age: Int?): Builder {
          this.age = age
          return this
        }
    
        /**
         * Returns a [Person] reference to the object being constructed by the builder.
         *
         * Throws an [IllegalArgumentException] when a non-null property wasn't initialised.
         *
         * @return Person
         */
        public fun build(): Person {
          if (name==null) {
          	throw IllegalArgumentException("Null name found when building Person.")
          }
          if (age==null) {
          	throw IllegalArgumentException("Null age found when building Person.")
          }
          return Person(name!!, nickname, age!!)
        }
      }
    }
    
    /**
     * Creates a [Person] through a DSL-style builder.
     *
     * @param initializer the initialisation block
     * @return Person
     */
    @JvmSynthetic
    public fun Person(initializer: Person.Builder.() -> Unit): Person =
        Person.Builder().apply(initializer).build()
    
""".trimIndent().replace('%', '$')
