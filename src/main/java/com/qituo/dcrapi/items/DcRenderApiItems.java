package com.qituo.dcrapi.items;

import com.qituo.dcrapi.DcRenderApi;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DcRenderApiItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DcRenderApi.MOD_ID);
    
    // 粒子测试器物品
    public static final RegistryObject<Item> PARTICLE_TESTER = ITEMS.register("particle_tester", () -> 
        new ParticleTesterItem(new Item.Properties().stacksTo(1))
    );
}
