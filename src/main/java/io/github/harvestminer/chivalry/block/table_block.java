package io.github.harvestminer.chivalry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class table_block extends Block
{
    private static final VoxelShape TOP = Block.box(0, 13, 0, 16, 16, 16);
    private static final VoxelShape LEG1 = Block.box(1, 0, 1, 3, 15, 3);
    private static final VoxelShape LEG2 = Block.box(1, 0, 13, 3, 15, 15);
    private static final VoxelShape LEG3 = Block.box(13, 0, 13, 15, 15, 15);
    private static final VoxelShape LEG4 = Block.box(13, 0, 1, 15, 15, 3);
    private static final VoxelShape ShapeFinal = Shapes.or(TOP, LEG1, LEG2, LEG3, LEG4);

    public table_block(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter player, BlockPos pos, CollisionContext context) {
        return ShapeFinal;
    }
}
