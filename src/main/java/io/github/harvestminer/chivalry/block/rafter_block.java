package io.github.harvestminer.chivalry.block;

import com.google.common.collect.ImmutableMap;
import io.github.harvestminer.chivalry.Chivalry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class rafter_block extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty TIP = BooleanProperty.create("tip");
    public static final BooleanProperty POST = BooleanProperty.create("post");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final Map<BlockState, VoxelShape> shapeByIndex;
    private final Map<BlockState, VoxelShape> collisionShapeByIndex;

    private static final VoxelShape TIP_SHAPE = Block.box(3, 9, 3.05, 13, 17.25, 13.05);
    private static final VoxelShape POST_SHAPE = Block.box(3.75, 0, 3.75, 12.25, 16, 12.25);
    private static final VoxelShape NORTH_SHAPE = Block.box(4, 5, 0, 12, 16, 8);
    private static final VoxelShape SOUTH_SHAPE = Block.box(4, 5, 8, 12, 16, 16);
    private static final VoxelShape EAST_SHAPE = Block.box(8, 5, 4, 16, 16, 12);
    private static final VoxelShape WEST_SHAPE = Block.box(0, 5, 4, 8, 16, 12);

    public rafter_block(Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(TIP, true)
                        .setValue(POST, true)
                        .setValue(NORTH, false)
                        .setValue(SOUTH, false)
                        .setValue(EAST, false)
                        .setValue(WEST, false)
                        .setValue(WATERLOGGED, Boolean.valueOf(false))
        );

        this.shapeByIndex = this.makeShapes();
        this.collisionShapeByIndex = this.makeShapes();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter player, BlockPos position, CollisionContext context) {
        return this.shapeByIndex.get(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter player, BlockPos position, CollisionContext context) {
        return this.collisionShapeByIndex.get(state);
    }

    private Map<BlockState, VoxelShape> makeShapes() {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        for (Boolean tip : TIP.getPossibleValues()) {
            for (Boolean post : POST.getPossibleValues()) {
                for (Boolean north : NORTH.getPossibleValues()) {
                    for (Boolean south : SOUTH.getPossibleValues()) {
                        for (Boolean east : EAST.getPossibleValues()) {
                            for (Boolean west : WEST.getPossibleValues()) {
                                for (Boolean water : WATERLOGGED.getPossibleValues()) {
                                    VoxelShape shapes = Shapes.empty();

                                    if (tip) shapes = Shapes.or(shapes, TIP_SHAPE);
                                    if (post) shapes = Shapes.or(shapes, POST_SHAPE);
                                    if (north) shapes = Shapes.or(shapes, NORTH_SHAPE);
                                    if (south) shapes = Shapes.or(shapes, SOUTH_SHAPE);
                                    if (east) shapes = Shapes.or(shapes, EAST_SHAPE);
                                    if (west) shapes = Shapes.or(shapes, WEST_SHAPE);

                                    BlockState blockstate = this.defaultBlockState()
                                            .setValue(TIP, Boolean.valueOf(tip))
                                            .setValue(POST, Boolean.valueOf(post))
                                            .setValue(NORTH, Boolean.valueOf(north))
                                            .setValue(SOUTH, Boolean.valueOf(south))
                                            .setValue(EAST, Boolean.valueOf(east))
                                            .setValue(WEST, Boolean.valueOf(west))
                                            .setValue(WATERLOGGED, Boolean.valueOf(water));

                                    builder.put(blockstate, shapes);
                                }
                            }
                        }
                    }
                }
            }
        }

        return builder.build();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos position = context.getClickedPos();
        FluidState fluidstate = level.getFluidState(position);
        BlockState blockState = this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
        return this.updateShape(blockState, level, position);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState oldState, LevelAccessor level, BlockPos position, BlockPos oldPosition) {
        return this.updateShape(state, level, position);
    }

    public BlockState updateShape(BlockState state, LevelAccessor level, BlockPos position)
    {
        BlockPos bNorth = position.north();
        BlockPos bSouth = position.south();
        BlockPos bEast = position.east();
        BlockPos bWest = position.west();
        BlockPos bUp = position.above();
        BlockPos bDown = position.below();

        BlockState bsNorth = level.getBlockState(bNorth);
        BlockState bsSouth = level.getBlockState(bSouth);
        BlockState bsEast = level.getBlockState(bEast);
        BlockState bsWest = level.getBlockState(bWest);
        BlockState bsUp = level.getBlockState(bUp);
        BlockState bsDown = level.getBlockState(bDown);

        boolean north = bsNorth.is(this);
        boolean south = bsSouth.is(this);
        boolean east = bsEast.is(this);
        boolean west = bsWest.is(this);
        boolean down = (bsDown.is(this) && bsDown.getValue(POST)) || !bsDown.is(this) && !bsDown.is(Blocks.AIR);
        boolean post = down || (!west && !east) && (!north && !south);
        boolean tip = bsUp.is(Blocks.AIR) && post;

        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(position, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return this.defaultBlockState()
                .setValue(TIP, tip)
                .setValue(POST, post)
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(EAST, east)
                .setValue(WEST, west);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter player, BlockPos position) {
        return !state.getValue(WATERLOGGED);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(TIP, POST, NORTH, SOUTH, EAST, WEST, WATERLOGGED);
    }
}
