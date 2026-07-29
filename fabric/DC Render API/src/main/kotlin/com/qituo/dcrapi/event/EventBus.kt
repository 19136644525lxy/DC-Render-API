package com.qituo.dcrapi.event

import java.util.concurrent.CopyOnWriteArrayList

class EventBus {
    private val listeners = mutableMapOf<Class<out Event>, CopyOnWriteArrayList<(Event) -> Unit>>()
    
    fun <T : Event> register(eventClass: Class<T>, listener: (T) -> Unit) {
        listeners.computeIfAbsent(eventClass) { CopyOnWriteArrayList() }
            .add { listener(it as T) }
    }
    
    fun post(event: Event) {
        listeners[event.javaClass]?.forEach { it(event) }
    }
    
    fun clear() {
        listeners.clear()
    }
}
