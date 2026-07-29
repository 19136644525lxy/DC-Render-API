package com.qituo.dcrapi.items;

import com.qituo.dcrapi.DcRenderApi;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * DC Render API 创造物品栏
 *
 * 原理：Forge 用 CreativeModeTab.builder() + DeferredRegister；
 *      Fabric 1.20.1 中通过 Registry.register 直接注册 ItemGroup，
 *      并使用 FabricItemSettings.tab() 指定所属组。
 */
public class DcRenderApiCreativeTab {

    // 渲染API物品创造栏
    public static final ItemGroup RENDER_API_TAB = Registry.register(
        Registries.ITEM_GROUP,
        new Identifier(DcRenderApi.MOD_ID, "render_api_items"),
        ItemGroup.create(ItemGroup.Row.TOP, 0)
            .displayName(Text.translatable("itemGroup.dcrapi.render_api_items"))
            .icon(() -> new ItemStack(DcRenderApiItems.PARTICLE_TESTER))
            .entries((displayContext, entries) -> {
                entries.add(DcRenderApiItems.PARTICLE_TESTER);
            })
            .build()
    );

    /**
     * 静态初始化
     */
    public static void initialize() {
        DcRenderApi.LOGGER.info("DC Render API creative tab registered");
    }
}
