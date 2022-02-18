package com.tobrun.datacompat.annotation

/**
 * Annotation class of DataCompat.
 * Classes annotated with this annotation are required to be Kotlin data classes with private visibility.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DataCompat