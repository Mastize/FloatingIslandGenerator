package com.example.examplemod;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";
    public FloatingIslandGeneratorSimpleNoise island = new FloatingIslandGeneratorSimpleNoise();
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerWorldGenerator(island, 1);
    }
}
