package com.mjolkster.mjolksters_winery.common.handler;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class VoxelShapeHandler {
    public static final Map<String, Map<Direction, VoxelShape>> SHAPE_MAPS = new HashMap<>();

    static {
        registerShapeMap("bottling_machine", makeDirectionalShapes(new AABB(1 / 16.0, 0, 8 / 16.0, 15 / 16.0, 11 / 16.0, 1.0)));
        registerShapeMap("aging_barrel", makeDirectionalShapes(new AABB(2 / 16.0, 0, 2 / 16.0, 14 / 16.0, 14 / 16.0, 1.0)));
        registerShapeMap("crusher", makeDirectionalShapes(new AABB(3 / 16.0, 0, 3 / 16.0, 13 / 16.0, 9 / 16.0, 13 / 16.0)));
        registerShapeMap("sommeliers_table", makeDirectionalShapes(new AABB(0, 0.001, 0, 1, 12 / 16.0, 1)));
        registerShapeMap("rack", makeDirectionalShapes(new AABB(0, 0.001, 7/16f, 1, 1, 1)));
    }

    public static Map<Direction, VoxelShape> makeDirectionalShapes(AABB baseBox) {
        Map<Direction, VoxelShape> map = new EnumMap<>(Direction.class);
        map.put(Direction.NORTH, Shapes.create(baseBox));
        map.put(Direction.EAST, Shapes.create(rotateBoxAroundCenter(baseBox, 90)));
        map.put(Direction.SOUTH, Shapes.create(rotateBoxAroundCenter(baseBox, 180)));
        map.put(Direction.WEST, Shapes.create(rotateBoxAroundCenter(baseBox, 270)));
        return map;
    }

    public static void registerShapeMap(String blockId, Map<Direction, VoxelShape> shapes) {
        SHAPE_MAPS.put(blockId, shapes);
    }

    public static VoxelShape getVoxelShape(String blockId, DirectionProperty directionProperty, Direction direction) {
        Map<Direction, VoxelShape> map = SHAPE_MAPS.get(blockId);
        if (map == null) {
            throw new IllegalStateException("No shape map registered for block: " + blockId);
        }
        VoxelShape shape = map.get(direction);
        if (shape == null) {
            throw new IllegalArgumentException("No shape defined for direction: " + direction + " in block: " + blockId);
        }
        return shape;
    }

    public static AABB rotateBoxAroundCenter(AABB box, int degrees) {

        // Block centre (fractional)
        double cx = 0.5;
        double cz = 0.5;
        double radians = Math.toRadians(degrees);

        BiFunction<Double, Double, double[]> rotate = (x, z) -> {
            double dx = x - cx;
            double dz = z - cz;
            double rx = cx + dx * Math.cos(radians) - dz * Math.sin(radians);
            double rz = cz + dx * Math.sin(radians) + dz * Math.cos(radians);
            return new double[]{rx, rz};
        };

        // Rotate the corners
        double[] minMin = rotate.apply(box.minX, box.minZ);
        double[] minMax = rotate.apply(box.minX, box.maxZ);
        double[] maxMin = rotate.apply(box.maxX, box.minZ);
        double[] maxMax = rotate.apply(box.maxX, box.maxZ);

        // Find new bounds
        double minX = min(minMin[0], minMax[0], maxMin[0], maxMax[0]);
        double maxX = max(minMin[0], minMax[0], maxMin[0], maxMax[0]);
        double minZ = min(minMin[1], minMax[1], maxMin[1], maxMax[1]);
        double maxZ = max(minMin[1], minMax[1], maxMin[1], maxMax[1]);

        // Et Voila
        return new AABB(minX, box.minY, minZ, maxX, box.maxY, maxZ);
    }

    private static double min(double... values) {
        double min = values[0];
        for (double v : values) min = Math.min(min, v);
        return min;
    }

    private static double max(double... values) {
        double max = values[0];
        for (double v : values) max = Math.max(max, v);
        return max;
    }

}