package com.tobrun.data.compat.example

import com.tobrun.datacompat.annotation.DataCompat

@DataCompat
private data class PersonData(val name: String, val nickname: String? = null, val age: Int)