package com.qituo.dcrapi.config

object ConfigManager {
    private var config: Config? = null
    
    fun setConfig(cfg: Config) {
        config = cfg
        cfg.load()
    }
    
    fun getConfig(): Config? {
        return config
    }
    
    @JvmStatic
    fun get(key: String): Any? {
        return config?.get(key)
    }
    
    @JvmStatic
    fun set(key: String, value: Any) {
        config?.set(key, value)
    }
    
    @JvmStatic
    fun save() {
        config?.save()
    }
}
