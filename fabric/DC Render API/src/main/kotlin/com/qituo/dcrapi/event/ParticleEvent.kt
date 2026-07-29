package com.qituo.dcrapi.event

/**
 * 粒子事件基类
 * 所有粒子相关事件的父类
 */
abstract class ParticleEvent {
    /**
     * 事件是否被取消
     */
    var isCanceled: Boolean = false
        protected set
    
    /**
     * 取消事件
     */
    fun cancel() {
        isCanceled = true
    }
}
