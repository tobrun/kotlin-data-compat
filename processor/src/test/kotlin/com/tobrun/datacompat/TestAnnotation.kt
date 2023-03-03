package com.tobrun.datacompat

import com.tschuchort.compiletesting.SourceFile

internal val testAnnotation = SourceFile.kotlin(
    "TestAnnotation.kt",
"""
package com.tobrun.datacompat.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DataCompat
    """.trimIndent()
)
