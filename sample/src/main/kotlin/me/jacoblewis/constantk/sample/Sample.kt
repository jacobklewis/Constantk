package me.jacoblewis.constantk.sample

import me.jacoblewis.constantk.AndroidAsset
import me.jacoblewis.constantk.NamedConstant
import me.jacoblewis.constantk.sample.PersonNamedConst.A_THING
import java.io.File


fun main() {
    println("Hello World")
}

class Dog {

    @AndroidAsset("text/dog.txt")
    val dogText = File(DogAssets.GET_DOG_TEXT)

}

class Person {
    val db = mutableMapOf<String, Any>()
    fun saveUser(@NamedConstant firstName: String, @NamedConstant lastName: String) {
        db[PersonNamedConst.FIRST_NAME] = firstName
        db[PersonNamedConst.LAST_NAME] = firstName
    }

    @NamedConstant("a thing")
    val aThing: String = A_THING
}



