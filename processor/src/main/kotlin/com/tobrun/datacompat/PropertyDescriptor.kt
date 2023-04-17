package com.tobrun.datacompat

import com.squareup.kotlinpoet.TypeName

data class PropertyDescriptor(
    val typeName: TypeName,
    val mandatoryForConstructor: Boolean,
    val kDoc: String,
    val hasActualKDoc: Boolean,
)
