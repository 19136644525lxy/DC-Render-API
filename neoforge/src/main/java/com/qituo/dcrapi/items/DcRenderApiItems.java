package com.qituo.dcrapi.items;

import com.qituo.dcrapi.DCRenderAPI;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DcRenderApiItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DCRenderAPI.MODID);

    public static final DeferredItem<Item> PARTICLE_TESTER = ITEMS.register("particle_tester",
        () -> new ParticleTesterItem(new Item.Properties().stacksTo(1)));
}
