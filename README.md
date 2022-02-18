## Data-compat

### Context
The Data-compat library resolves the java binary incompatibility that library developers face when using Kotlin data classes.
In a nutshell, the issue that library developers have when using data classes in combination with supporting the Java language, is that every change to a data class definition will result in major breaking change. This project attempts to resolve this binary incompatibility by metaprogramming using annotaitons. Users can still rely on using data classes, but the generated implementation will comply for Java comsumption.

To read more about this incompatibility, please refer to Jake Wharton's [Public API challenges in Kotlin](https://jakewharton.com/public-api-challenges-in-kotlin/) blogpost.

### How
Data-compat uses [Kotlin Symbol Processing API (KSP)](https://kotlinlang.org/docs/ksp-overview.html) in combination with [KotlinPoet](https://square.github.io/kotlinpoet/) to generate Kotlin classes that are Java binary compatible. Input for the system is a private data class that supports a `@DataCompat` annotation. The code generator will output a class that supports a builder pattern.

#### Input

```kotlin
@DataCompat
private data class PersonData(val name: String, val nickname: String? = null, val age: Int)
```

#### Output

```kotlin
class Person private constructor(
  val name: String,
  val nickname: String?,
  val age: Int
) {
  override fun toString() = "Person(name=$name, nickname=$nickname, age=$age)"
  override fun equals(other: Any?) = other is Person
      && name == other.name
      && nickname == other.nickname
      && age == other.age
  override fun hashCode() = Objects.hash(name, nickname, age)

  class Builder {
    @set:JvmSynthetic // Hide 'void' setter from Java
    var name: String? = null
    @set:JvmSynthetic // Hide 'void' setter from Java
    var nickname: String? = null
    @set:JvmSynthetic // Hide 'void' setter from Java
    var age: Int = 0

    fun setName(name: String?) = apply { this.name = name }
    fun setNickname(nickname: String?) = apply { this.nickname = nickname }
    fun setAge(age: Int) = apply { this.age = age }

    fun build() = Person(name!!, nickname, age)
  }
}

@JvmSynthetic // Hide from Java callers who should use Builder.
fun Person(initializer: Person.Builder.() -> Unit): Person {
  return Person.Builder().apply(initializer).build()
}
```

### State of project

This project is under development, the following still needs to be done:
 - [ ] integrate output compatible with above using Kotlin Poet
 - [ ] split up annotation from processor module?
 - [ ] integrate example shown above
 - [ ] add unit tests
 - [ ] publish to maven
