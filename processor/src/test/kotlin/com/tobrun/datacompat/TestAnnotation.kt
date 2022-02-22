package com.tobrun.datacompat

import com.tschuchort.compiletesting.SourceFile

internal const val TEST_ANNOTATION_FILE_NAME = "TestAnnotation.kt"

internal val testAnnotation = SourceFile.kotlin(
    TEST_ANNOTATION_FILE_NAME,
    """package com.tobrun.datacompat.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DataCompat
"""
)
