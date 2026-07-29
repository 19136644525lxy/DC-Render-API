package com.qituo.dcrapi.items;

import com.qituo.dcrapi.DcRenderApi;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * DC Render API 物品注册
 *
 * 原理：Forge 用 DeferredRegister + RegistryObject 延迟注册，
 *      Fabric 直接使用 Registry.register 立即注册。
 */
public class DcRenderApiItems {

    // 粒子测试器物品
    public static final Item PARTICLE_TESTER = register(
        "particle_tester",
        new ParticleTesterItem(new Item.Settings().maxCount(1))
    );

    /**
     * 注册物品
     */
    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(DcRenderApi.MOD_ID, name), item);
    }

    /**
     * 静态初始化（在主类 onInitialize 中调用以触发类加载）
     */
    public static void initialize() {
        DcRenderApi.LOGGER.info("DC Render API items registered");
    }
}
