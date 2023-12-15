package io.github.harvestminer.chivalry.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class chair_block extends Block
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape SHAPE_COMMON = Shapes.or(
            Block.box(3, 9, 3, 13, 10, 13),
            Block.box(4, 0, 4, 6, 9, 6),
            Block.box(4, 0, 10, 6, 9, 12),
            Block.box(10, 0, 4, 12, 9, 6),
            Block.box(10, 0, 10, 12, 9, 12)
    );

    public static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(4, 10, 11, 12, 21, 12),
            SHAPE_COMMON
    );

    public static final VoxelShape SHAPE_SOUTH = Shapes.or(
            Block.box(4, 10, 4, 12, 21, 5),
            SHAPE_COMMON
    );

    public static final VoxelShape SHAPE_EAST = Shapes.or(
            Block.box(4, 10, 4, 5, 21, 12),
            SHAPE_COMMON
    );

    public static final VoxelShape SHAPE_WEST = Shapes.or(
            Block.box(11, 10, 4, 12, 21, 12),
            SHAPE_COMMON
    );

    public chair_block(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.getStateDefinition().any().setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation transform) {
        return state.setValue(FACING, transform.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror transform) {
        return state.rotate(transform.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_54562_, BlockPos p_54563_, CollisionContext p_54564_) {
        switch((Direction)state.getValue(FACING)) {
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_COMMON;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING);
    }
}
