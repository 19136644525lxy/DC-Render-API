package com.qituo.dcrapi.items;

import com.qituo.dcrapi.DCRenderAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DcRenderApiCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DCRenderAPI.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RENDER_API_TAB =
        CREATIVE_MODE_TABS.register("render_api_items", () -> CreativeModeTab.builder()
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
