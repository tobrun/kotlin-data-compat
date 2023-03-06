package com.tobrun.datacompat.annotation

/**
 * Default value represented as a String.
 * Workaround for KSP not supporting class default parameters:
 * https://github.com/google/ksp/issues/642
 *
 * @param valueAsString exact representation of the default value. E.g. if default [String] is used,
 *  it should be passed here as "\"STRING_VALUE\""; if default [Int] is used, it should be passed
 *  as "INT_VALUE".
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Default(val valueAsString: String)
