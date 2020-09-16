/*
 * Copyright (C) 2020 Jacob Lewis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.jacoblewis.constantk

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class NamedConstantGenerator : AbstractProcessor() {

    var logger: Messager? = null

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(NamedConstant::class.java.name)
    }

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        logger = processingEnv?.messager
    }

    fun note(message: Any?) {
        message ?: return
        logger?.printMessage(Diagnostic.Kind.NOTE, message.toString())
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        val namedConstants = roundEnv.getElementsAnnotatedWith(NamedConstant::class.java)
        note("number of items: ${namedConstants.size} \n ")
        // Iterate over each named constant and group by parent class
        val groupedNC = namedConstants.groupBy { nc ->
            note("processing item: ${nc.simpleName.toString()} \n ")
            if (nc.kind == ElementKind.CLASS) {
                nc.simpleName.toString()
            } else {
                var nEl = nc.enclosingElement
                while (nEl != null && (nEl.kind != ElementKind.CLASS || nEl.modifiers.contains(Modifier.STATIC))) {
                    note("skipping to parent for item: ${nEl.simpleName.toString()} kind: ${nEl.kind} \n ")
                    nEl = nEl.enclosingElement
                }
                (nEl ?: nc).simpleName.toString()
            }
        }

        for ((groupName, groupNC) in groupedNC) {
            val objName = "${groupName}NamedConst"
            val constNameValuePairs = groupNC.flatMap { el ->
                el.getAnnotationsByType(NamedConstant::class.java).map {
                    val elName = cleanupName(el.simpleName.toString(), it.removeGetterSetter)
                    val constValue = it.propertyName
                    val elAsValue = it.elemAsValue
                    val name = if (constValue.isBlank()) elName else constValue
                    val value = if (elAsValue) elName else name
                    return@map name to value
                }
            }

            val obj = with(TypeSpec.objectBuilder(objName)) {
                for (ncNameVals in constNameValuePairs) {
                    var constName =
                        ncNameVals.first.split(" ").joinToString { it.capitalize() }.filter { it.isLetterOrDigit() }
                            .camelToUpperSnakeCase()
                    while (constName.firstOrNull()?.isDigit() == true) {
                        constName = constName.substring(1)
                    }
                    if (constName.isBlank()) {
                        throw Exception("Constant ${ncNameVals.first} could not be formatted to a usable constant Name")
                    }
                    addProperty(
                        PropertySpec.builder(
                            constName,
                            ClassName("kotlin", "String"),
                            KModifier.CONST,
                            KModifier.FINAL
                        ).mutable(false).initializer("\"${ncNameVals.second}\"").build()
                    )
                }
                build()
            }

            val pack = processingEnv.elementUtils.getPackageOf(groupNC.first()).toString()
            val file = FileSpec.builder(pack, objName)
                .addType(obj).build()
            val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            file.writeTo(File(kaptKotlinGeneratedDir, "$objName.kt"))
        }

        return true
    }

    private fun cleanupName(rawName: String, removeGetterSetter: Boolean): String {
        return rawName.replace("\$annotations", "").let {
            if (removeGetterSetter) {
                it.removePrefix("get")
                    .removePrefix("set")
            } else {
                it
            }
        }
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}