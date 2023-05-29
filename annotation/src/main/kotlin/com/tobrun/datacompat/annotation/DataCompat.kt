package com.tobrun.datacompat.annotation

/**
 * Annotation class of DataCompat.
 * Classes annotated with this annotation are required to be Kotlin data classes with private visibility.
 *
 * @param importsForDefaults if any default values require additional imports, they should be passed here.
 *  E.g. `["android.graphics.Color"]`
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class DataCompat(val importsForDefaults: Array<String> = [])
