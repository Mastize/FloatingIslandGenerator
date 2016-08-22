package com.example.examplemod;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class GenFloatingIslandSimpleNoise extends WorldGenerator{

    private final int scale = 8;
    private final int unit = 8;
    private final int size = unit * scale + 1;
    private final int lowerBound = -100;
    private final int upperBound = 10;

    private final int stone = 1;
    private final int grass = 2;
    private final int dirt = 3;

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        int i, j, k;
        byte[][][] island = new byte[size][size][size];

        // generate random value at all 3d corner
        for(i = 0;i < size;i += unit) {
            for(k = 0;k < size;k += unit) {
                for(j = 0;j < size;j += unit) {
                    island[i][j][k] = (byte)(random.nextInt(upperBound - lowerBound) + lowerBound);
                }
            }
        }

        // fill all 3d unit block
        for(i = 0;i < size - 1;i += unit) {
            for(k = 0;k < size - 1;k += unit) {
                for(j = 0;j < size - 1;j += unit) {
                    fillThreeDimension(island, i, j, k, unit);
                }
            }
        }

        for(i = 0;i < size;i++) {
            for(k = 0;k < size;k++) {
                int flag = 0;
                for(j = size - 1;j >= 0;j--) {
                    double height = (upperBound - lowerBound) * ((double)Math.abs(j - size / 2) / (size / 2));
                    double distance = (upperBound - lowerBound) * 1.2 * ((double)Math.abs(i - size / 2) / size + (double)Math.abs(k - size / 2) / size);
                    if(island[i][j][k] + height + distance < 0 && y + j < 255){
                        if(flag == 0) {
                            world.setBlock(x + i, y + j, z + k, Block.getBlockById(grass));
                        } else if (flag < 3) {
                            world.setBlock(x + i, y + j, z + k, Block.getBlockById(dirt));
                        } else {
                            world.setBlock(x + i, y + j, z + k, Block.getBlockById(stone));
                        }
                        flag++;
                    }
                }
            }
        }
        return true;
    }

    private byte linear(byte x, byte y, int dx, int dy) {
        double result = x + (double)dx/(dx + dy) * (y - x);
        return (byte)result;
    }

    private void fillOneLine(byte[][][] content, int x1, int y1, int z1, int x2, int y2, int z2) {
        if(x1 != x2) {
            int start = Math.min(x1, x2);
            int end = Math.max(x1, x2);
            for(int i = start + 1;i < end;i++) {
                content[i][y1][z1] = linear(content[start][y1][z1], content[end][y2][z2], i - start, end - i);
            }
        } else if(y1 != y2) {
            int start = Math.min(y1, y2);
            int end = Math.max(y1, y2);
            for(int i = start + 1;i < end;i++) {
                content[x1][i][z1] = linear(content[x1][start][z1], content[x2][end][z2], i - start, end - i);
            }
        } else {
            int start = Math.min(z1, z2);
            int end = Math.max(z1, z2);
            for(int i = start + 1;i < end;i++) {
                content[x1][y1][i] = linear(content[x1][y1][start], content[x2][y2][end], i - start, end - i);
            }
        }
    }

    private void fillXYPlane(byte[][][] content, int x, int y, int z, int size) {
        fillOneLine(content, x, y, z, x+size, y, z);
        fillOneLine(content, x, y+size, z, x+size, y+size, z);
        for(int i = x;i <= x+size;i++) {
            fillOneLine(content, i, y, z, i, y+size, z);
        }
    }

    private void fillYZPlane(byte[][][] content, int x, int y, int z, int size) {
        fillOneLine(content, x, y, z, x, y, z+size);
        fillOneLine(content, x, y+size, z, x, y+size, z+size);
        for(int i = z;i <= z+size;i++) {
            fillOneLine(content, x, y, i, x, y+size, i);
        }
    }

    //from x y z to x+size y+size z+size
    private void fillThreeDimension(byte[][][] content, int x, int y, int z, int size) {
        fillXYPlane(content, x, y, z, size);
        fillXYPlane(content, x, y, z+size, size);
        for(int i = x;i <= x+size;i++) {
            fillYZPlane(content, i, y, z, size);
        }
    }
}
