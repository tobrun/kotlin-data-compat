package com.tobrun.datacompat.annotation

/**
 * Annotation class of DataCompat.
 * Classes annotated with this annotation are required to be Kotlin data classes with private visibility.
 *
 * @param importsForDefaults if any default values require additional imports, they should be passed here.
 *  E.g. `["android.graphics.Color"]`
 * @param generateCompanionObject if enabled, a public companion object will be generated for the class,
 *  so that user can then manually write extension functions to the Companion object. Defaults to false.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class DataCompat(
    val importsForDefaults: Array<String> = [],
    val generateCompanionObject: Boolean = false
)
