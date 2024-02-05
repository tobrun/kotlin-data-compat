package com.tobrun.datacompat

import DataCompatProcessorProvider
import com.tobrun.datacompat.utils.getGeneratedSource
import com.tobrun.datacompat.utils.kspGeneratedSources
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

private val simpleTest = SourceFile.kotlin(
    "PersonData.kt",
    """
import com.tobrun.datacompat.annotation.DataCompat
import com.tobrun.datacompat.annotation.Default

interface EmptyInterface
interface EmptyInterface2
/**
 * Represents a person.
 * @property name The full name.
 * @property nickname The nickname.
 * @property age The age.
 * @property veryLongAndVeryDetailedDescription The very long and very detailed description.
 */
@Deprecated
@DataCompat(importsForDefaults = ["java.util.Date"], generateCompanionObject = true)
private data class PersonData(
    @Default("\"John\"")
    val name: String,
    @Default("null")
    val nickname: String?,
    @Default("23")
    val age: Int,
    /**
     * Actually it's a very short description.
    */
    val veryLongAndVeryDetailedDescription: String?,
    /**
     * Parameter that will become constructor parameter.
    */
    val mandatoryDoubleWithoutDefault: Double,
) : EmptyInterface, EmptyInterface2
    """.trimIndent()
)

private class GeneratedFileData(
    val fileName: String,
    val fileContent: String,
)

private class CompatProcessorData(
    val title: String,
    val sourceFile: SourceFile,
    val expectedExitCode: ExitCode,
    val expectedGeneratedFile: List<GeneratedFileData>,
)

private val testData = listOf(
    CompatProcessorData(
        title = "Simple test",
        sourceFile = simpleTest,
        expectedExitCode = ExitCode.OK,
        expectedGeneratedFile = listOf(
            GeneratedFileData(
                "Person.kt",
                expectedSimpleTestContent,
            )
        )
    )
)

internal class DataCompatProcessorTest : BehaviorSpec({
    testData.forEach { data ->
        Given(data.title) {
            val compilation = KotlinCompilation().apply {
                sources = listOf(testAnnotation, data.sourceFile)
                symbolProcessorProviders = listOf(DataCompatProcessorProvider())
            }
            When("Generate ksp files") {
                val result = compilation.compile()
                Then("exit code should be ${data.expectedExitCode}") {
                    result.exitCode shouldBe ExitCode.OK
                }
                Then("generated list size should be ${data.expectedGeneratedFile.size}") {
                    result.kspGeneratedSources.size shouldBe data.expectedGeneratedFile.size
                }
                data.expectedGeneratedFile.forEachIndexed { index, generatedFileData ->
                    Then("file name should be ${generatedFileData.fileName}") {
                        result.kspGeneratedSources[index].name shouldBe generatedFileData.fileName
                    }
                    Then("file content should be ${generatedFileData.fileContent}") {
                        result.getGeneratedSource(index) shouldBe generatedFileData.fileContent
                    }
                }
            }
        }
    }
})
