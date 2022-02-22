package com.tobrun.data.compat.example

import com.tobrun.datacompat.annotation.DataCompat

/**
 * Represents a person.
 * @property name The full name.
 * @property nickname The nickname.
 * @property age The age.
 */
@DataCompat
private data class PersonData(val name: String, val nickname: String? = null, val age: Int)