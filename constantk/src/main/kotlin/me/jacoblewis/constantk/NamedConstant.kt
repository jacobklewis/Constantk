package me.jacoblewis.constantk

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS,
        AnnotationTarget.FIELD,
        AnnotationTarget.PROPERTY,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER)
annotation class NamedConstant(
        val propertyName: String = "",
        val elemAsValue: Boolean = false
)