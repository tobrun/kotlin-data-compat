package com.tobrun.data.compat.example

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