package com.qituo.dcrapi.config

interface Config {
    fun load()
    fun save()
    fun get(key: String): Any?
    fun set(key: String, value: Any)
}