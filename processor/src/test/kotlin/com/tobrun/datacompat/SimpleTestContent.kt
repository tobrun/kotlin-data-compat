package com.tobrun.datacompat

internal val expectedSimpleTestContent = """
    import java.util.Objects
    import kotlin.Any
    import kotlin.Boolean
    import kotlin.Int
    import kotlin.String
    import kotlin.Unit
    import kotlin.jvm.JvmSynthetic

    public class Person private constructor(
      public val name: String,
      public val nickname: String?,
      public val age: Int
    ) {
      public override fun toString() = "Person(name=%name, nickname=%nickname, age=%age)"

      public override fun equals(other: Any?): Boolean = other is Person
      		&& name == other.name
      		&& nickname == other.nickname
      		&& age == other.age

      public override fun hashCode(): Int = Objects.hash(name, nickname, age)

      public class Builder {
        @set:JvmSynthetic
        public var name: String? = null

        @set:JvmSynthetic
        public var nickname: String? = null

        @set:JvmSynthetic
        public var age: Int? = null

        public fun setName(name: String?): Builder {
          this.name = name
          return this
        }

        public fun setNickname(nickname: String?): Builder {
          this.nickname = nickname
          return this
        }

        public fun setAge(age: Int?): Builder {
          this.age = age
          return this
        }

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

    @JvmSynthetic
    public fun Person(initializer: Person.Builder.() -> Unit): Person =
        Person.Builder().apply(initializer).build()
    
""".trimIndent().replace('%', '$')
