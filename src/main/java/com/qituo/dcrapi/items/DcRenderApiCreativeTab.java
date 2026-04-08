package com.qituo.dcrapi.items;

import com.qituo.dcrapi.DcRenderApi;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DcRenderApiCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DcRenderApi.MOD_ID);
    
    // 渲染API物品创造栏
    public static final RegistryObject<CreativeModeTab> RENDER_API_TAB = CREATIVE_MODE_TABS.register("render_api_items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.dcrapi.render_api_items"))
            .icon(() -> new ItemStack(DcRenderApiItems.PARTICLE_TESTER.get()))
            .displayItems((parameters, output) -> {
                output.accept(DcRenderApiItems.PARTICLE_TESTER.get());
            })
            .build());
    
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
