package com.gse.test.observerproyect.util

object Util {
    //getRandomNumberInRange
    fun getRandomNumberInRange(min: Int, max: Int): Int {
        return (Math.random() * (max - min) + min).toInt()
    }
}