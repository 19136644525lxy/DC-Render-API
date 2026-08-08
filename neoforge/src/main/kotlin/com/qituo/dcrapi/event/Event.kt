package com.qituo.dcrapi.event

open class Event {
    var isCanceled = false
    
    fun cancel() {
        isCanceled = true
    }
}