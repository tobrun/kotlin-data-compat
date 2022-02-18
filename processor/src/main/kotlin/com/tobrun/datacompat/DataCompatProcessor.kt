package com.tobrun.datacompat

import com.google.devtools.ksp.isPrivate
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import com.tobrun.datacompat.annotation.DataCompat

class DataCompatProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("DataCompatProcessor: generating code")
        val annotated = resolver.getSymbolsWithAnnotation(DataCompat::class.qualifiedName!!, true)
        if (annotated.count() == 0) {
            logger.warn("No DataCompat annotations found for processing")
            return emptyList()
        }

        val unableToProcess = annotated.filterNot { it.validate() }
        annotated.filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(Visitor(), Unit) }
        return unableToProcess.toList()
    }

    private inner class Visitor : KSVisitorVoid() {

        private lateinit var ksType: KSType
        private lateinit var packageName: String

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val qualifiedName = classDeclaration.qualifiedName?.asString() ?: run {
                logger.error(
                    "@DataClass must target classes with a qualified name",
                    classDeclaration
                )
                return
            }

            if (!classDeclaration.isDataClass()) {
                logger.error(
                    "@DataClass cannot target a non-data class $qualifiedName",
                    classDeclaration
                )
                return
            }

            if (!classDeclaration.isPrivate()) {
                logger.error(
                    "@DataClass target must have private visibility",
                    classDeclaration
                )
                return
            }

            if (classDeclaration.typeParameters.any()) {
                logger.error(
                    "@DataClass target shouldn't have type parameters",
                    classDeclaration
                )
                return
            }

            val classDeclarationName = classDeclaration.simpleName.asString()
            if (!classDeclarationName.endsWith("Data")) {
                logger.error(
                    "@DataClass target must end with Data suffix naming",
                    classDeclaration
                )
                return
            }

            ksType = classDeclaration.asType(emptyList())
            packageName = classDeclaration.packageName.asString()

            classDeclaration.getAllProperties()
                .forEach {
                    it.accept(this, Unit)
                }

            // Cleanup class name by dropping Data part
            // TODO make this part more flexible with providing name inside the annotation
            val className = classDeclarationName.dropLast(4)
            // TODO add actual implementation conform to https://jakewharton.com/public-api-challenges-in-kotlin/
            val fileKotlinPoet = FileSpec.builder(packageName, className)
                .addType(
                    TypeSpec.classBuilder(className)
                        .addProperty(
                            PropertySpec.builder("enabled", Boolean::class)
                                .initializer(options["enabled"] ?: "false")
                                .build()
                        )
                        .build()
                )
                .build()

            fileKotlinPoet.writeTo(codeGenerator = codeGenerator, aggregating = false)
        }
    }

    private fun KSClassDeclaration.isDataClass() = modifiers.contains(Modifier.DATA)
}
