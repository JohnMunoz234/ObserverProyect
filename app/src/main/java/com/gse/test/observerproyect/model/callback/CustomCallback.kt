package com.gse.test.observerproyect.model.callback

interface CustomCallback {
    fun onSuccess(`object`: Any?)

    fun onError(error: String?)
}