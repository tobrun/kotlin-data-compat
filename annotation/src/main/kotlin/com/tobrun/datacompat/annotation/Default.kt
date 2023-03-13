package com.tobrun.datacompat.annotation

/**
 * Default value represented as a String.
 * Workaround for KSP not supporting class default parameters:
 * https://github.com/google/ksp/issues/642
 *
 * Could be applied only for passing default values in the [DataCompat] class.
 *
 * @param valueAsString exact representation of the default value. E.g. if default [String] is used,
 *  it should be passed here as "\"STRING_VALUE\""; if default [Int] is used, it should be passed
 *  as "INT_VALUE".
 *
 * @param imports if default parameter requires additional imports, they should be passed here.
 *  E.g. `listOf("android.graphics.Color")`
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Default(
    val valueAsString: String,
    val imports: Array<String> = []
)
