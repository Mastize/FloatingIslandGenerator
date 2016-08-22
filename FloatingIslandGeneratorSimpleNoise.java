package com.example.examplemod;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

public class FloatingIslandGeneratorSimpleNoise implements IWorldGenerator {

    GenFloatingIslandSimpleNoise genIsland = new GenFloatingIslandSimpleNoise();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider iChunkProvider, IChunkProvider iChunkProvider1) {
        if(world.provider.getDimensionName().equals("Overworld") && random.nextDouble() < 0.005) {
            genIsland.generate(world, random, chunkX * 16 + random.nextInt(16), 100, chunkZ * 16 + random.nextInt(16));
        }
    }
}
