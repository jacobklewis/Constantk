package me.jacoblewis.constantk.sample

import me.jacoblewis.constantk.NamedConstant


fun main() {
    println("Hello World")
    println(DogNamedConst.USER_FRIENDLY_NAMES)
    println(PersonNamedConst.TAG)
}

class Dog {

    @NamedConstant("userFriendlyNames")
    val userNames: String
        get() = "Name1, Name2"
}



