package com.tobrun.data.compat.example

class KotlinTest {

    fun test(){
        val person = Person(extraInfo = "Extra info") {
            name = "Tobrun Van Nulamd"
            nickname = "Nurbot"
            age = 31
        }
    }
}