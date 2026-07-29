package com.qituo.dcrapi.config

import java.io.File
import java.util.Properties

/**
 * DC Render API 配置管理器
 */
object DcRenderApiConfig {
    
    private val config = Properties()
    private var configFile: File? = null
    
    // 配置项
    var maxParticleGroups: Int = 1000
        private set
    
    var defaultVisibleRange: Double = 64.0
        private set
    
    var particleTickRate: Int = 1
        private set
    
    var enableParticleEvents: Boolean = true
        private set
    
    var maxParticlesPerGroup: Int = 100
        private set
    
    var enableDebugLogging: Boolean = false
        private set
    
    /**
     * 加载配置文件
     */
    @JvmStatic
    fun load(file: File) {
        configFile = file
        
        if (!file.exists()) {
            save(file)
            return
        }
        
        try {
            file.inputStream().use { input ->
                config.load(input)
                
                // 读取配置项
                maxParticleGroups = config.getProperty("maxParticleGroups", "1000").toInt()
                defaultVisibleRange = config.getProperty("defaultVisibleRange", "64.0").toDouble()
                particleTickRate = config.getProperty("particleTickRate", "1").toInt()
                enableParticleEvents = config.getProperty("enableParticleEvents", "true").toBoolean()
                maxParticlesPerGroup = config.getProperty("maxParticlesPerGroup", "100").toInt()
                enableDebugLogging = config.getProperty("enableDebugLogging", "false").toBoolean()
            }
        } catch (e: Exception) {
            System.err.println("Failed to load config: ${e.message}")
        }
    }
    
    /**
     * 保存配置文件
     */
    @JvmStatic
    fun save(file: File) {
        try {
            // 更新配置项
            config.setProperty("maxParticleGroups", maxParticleGroups.toString())
            config.setProperty("defaultVisibleRange", defaultVisibleRange.toString())
            config.setProperty("particleTickRate", particleTickRate.toString())
            config.setProperty("enableParticleEvents", enableParticleEvents.toString())
            config.setProperty("maxParticlesPerGroup", maxParticlesPerGroup.toString())
            config.setProperty("enableDebugLogging", enableDebugLogging.toString())
            
            file.parentFile?.mkdirs()
            file.outputStream().use { output ->
                config.store(output, "DC Render API Configuration")
            }
        } catch (e: Exception) {
            System.err.println("Failed to save config: ${e.message}")
        }
    }
    
    /**
     * 重新加载配置
     */
    @JvmStatic
    fun reload() {
        configFile?.let { load(it) }
    }
    
    /**
     * 设置配置项（运行时修改）
     */
    fun setMaxParticleGroups(value: Int) {
        maxParticleGroups = value
    }
    
    fun setDefaultVisibleRange(value: Double) {
        defaultVisibleRange = value
    }
    
    fun setParticleTickRate(value: Int) {
        particleTickRate = value
    }
    
    fun setEnableParticleEvents(value: Boolean) {
        enableParticleEvents = value
    }
    
    fun setMaxParticlesPerGroup(value: Int) {
        maxParticlesPerGroup = value
    }
    
    fun setEnableDebugLogging(value: Boolean) {
        enableDebugLogging = value
    }
}
