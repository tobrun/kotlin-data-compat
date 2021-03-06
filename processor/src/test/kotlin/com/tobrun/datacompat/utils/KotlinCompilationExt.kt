package com.tobrun.datacompat.utils

import com.tschuchort.compiletesting.KotlinCompilation
import java.io.File

internal val KotlinCompilation.Result.kspGeneratedSources: List<File>
    get() {
        val kspWorkingDir = workingDir.resolve("ksp")
        val kspGeneratedDir = kspWorkingDir.resolve("sources")
        val kotlinGeneratedDir = kspGeneratedDir.resolve("kotlin")
        val javaGeneratedDir = kspGeneratedDir.resolve("java")
        return kotlinGeneratedDir.listFilesRecursively() + javaGeneratedDir.listFilesRecursively()
    }

internal fun KotlinCompilation.Result.getGeneratedSource(index: Int): String {
    return File(kspGeneratedSources[index].toPath().toString()).readText()
}

private val KotlinCompilation.Result.workingDir: File
    get() = outputDirectory.parentFile!!

private fun File.listFilesRecursively(): List<File> {
    return listFiles()?.flatMap { file ->
        if (file.isDirectory) {
            file.listFilesRecursively()
        } else {
            listOf(file)
        }
    } ?: emptyList()
}
