package com.tobrun.datacompat

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.TypeName

data class PropertyDescriptor(
    val typeName: TypeName,
    val mandatoryForConstructor: Boolean,
    val kDoc: String,
    val annotations: List<AnnotationSpec> = emptyList(),
)
