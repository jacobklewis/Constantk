# ConstantK
A lightweight library for conveniently generating constants for your Kotlin projects.

## Usage
### Android Asset Path Validation Constants
Validate Android Asset paths while building. This file would be placed under `app/src/main/assets/...`
```kotlin
@AndroidAsset("text/dog.txt")
val dogText = File(DOG_TEXT)
```
### On The Fly Constant Generation
Just add the `@NamedConstant` annotation to any class, parameter, or property to add an automatically generated constant. FYI: You do need to build the project before being able to use the constant.
```kotlin
val db = mutableMapOf<String, Any>()
fun saveUser(@NamedConstant firstName: String, @NamedConstant lastName: String) {
    db[FIRST_NAME] = firstName
    db[LAST_NAME] = firstName
}
```

## Setup
```groovy
def constantk_version = '0.2.1'
dependencies {
    kapt "me.jacoblewis.constantk:constantk-generator:$constantk_version"
    compileOnly "me.jacoblewis.constantk:constantk:$constantk_version"
}
```