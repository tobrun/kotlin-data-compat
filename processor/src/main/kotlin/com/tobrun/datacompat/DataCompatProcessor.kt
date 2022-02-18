package com.tobrun.datacompat

import com.google.devtools.ksp.isPrivate
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.toTypeParameterResolver
import com.squareup.kotlinpoet.ksp.writeTo
import com.tobrun.datacompat.annotation.DataCompat
import java.util.Objects
import kotlin.math.log

/**
 * [DataCompatProcessor] is a concrete instance of the [SymbolProcessor] interface.
 * This processor supports multiple round execution, it may return a list of deferred DataCompat annotated symbols.
 * Exceptions or implementation errors will result in a termination of processing immediately and be logged as an error
 * in KSPLogger.
 */
class DataCompatProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.info("DataCompatProcessor: process")
        val annotated = resolver.getSymbolsWithAnnotation(DataCompat::class.qualifiedName!!, true)
        if (annotated.count() == 0) {
            logger.info("No DataCompat annotations found for processing")
            return emptyList()
        }

        val unableToProcess = annotated.filterNot { it.validate() }
        annotated.filter { it is KSClassDeclaration && it.validate() }.forEach { it.accept(Visitor(), Unit) }
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

            // Align KSP properties with KoltinPoet TypeNames
            val propertyTypeMap = mutableMapOf<KSPropertyDeclaration, TypeName>()
            val properties = classDeclaration.getAllProperties()
            for (property in properties) {
                val classTypeParams = classDeclaration.typeParameters.toTypeParameterResolver()
                val typeName = property.type.resolve().toTypeName(classTypeParams)
                propertyTypeMap[property] = typeName
            }

            // KotlinPoet class builder
            val classBuilder = TypeSpec.classBuilder(className).apply {

                // Constructor
                val constructorBuilder = FunSpec.constructorBuilder()
                constructorBuilder.addModifiers(KModifier.PRIVATE)
                for (entry in propertyTypeMap) {
                    constructorBuilder.addParameter(entry.key.toString(), entry.value)
                }
                primaryConstructor(constructorBuilder.build())

                // Property initializers
                for (entry in propertyTypeMap) {
                    addProperty(PropertySpec.builder(entry.key.toString(), entry.value)
                        .initializer(entry.key.toString())
                        .build())
                }

                // Function toString
                addFunction(FunSpec.builder("toString")
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement(propertyTypeMap.keys.joinToString(
                        prefix = "return \"$className(",
                        transform = { "$it=$$it" },
                        postfix = ")\""
                    ))
                    .build()
                )

                // Function hashCode
                addFunction(FunSpec.builder("hashCode")
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement(propertyTypeMap.keys.joinToString(
                        prefix = "return Objects.hash(",
                        separator = ", ",
                        postfix = ")"
                    ))
                    .returns(Int::class)
                    .build()
                )

            }

            // TODO add actual implementation conform to https://jakewharton.com/public-api-challenges-in-kotlin/
            val fileKotlinPoet = FileSpec.builder(packageName, className)
                .addImport("java.util", "Objects")
                .addType(classBuilder.build())
                .build()

            fileKotlinPoet.writeTo(codeGenerator = codeGenerator, aggregating = false)
        }
    }

    private fun KSClassDeclaration.isDataClass() = modifiers.contains(Modifier.DATA)
}