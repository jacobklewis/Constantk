ConstantK
=====
![Logo](website/constant-k-icon.png)

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

Download
------
```groovy
def constantk_version = '0.2.1'
dependencies {
    kapt "me.jacoblewis.constantk:constantk-generator:$constantk_version"
    compileOnly "me.jacoblewis.constantk:constantk:$constantk_version"
}
```

License
-------

    Copyright 2020 Jacob Lewis
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
