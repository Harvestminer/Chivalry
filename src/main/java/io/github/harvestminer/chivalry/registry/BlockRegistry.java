package io.github.harvestminer.chivalry.registry;

import io.github.harvestminer.chivalry.Chivalry;
import io.github.harvestminer.chivalry.block.chair_block;
import io.github.harvestminer.chivalry.block.rafter_block;
import io.github.harvestminer.chivalry.block.table_block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegistry
{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Chivalry.MODID);

    public static final DeferredBlock[] TABLES = RegisterForAllWoodTypes(
            "table",
            table_block::new,
            new Item.Properties());

    public static final DeferredBlock[] CHAIRS = RegisterForAllWoodTypes(
            "chair",
            chair_block::new,
            new Item.Properties());

    public static final DeferredBlock[] RAFTER = RegisterForAllWoodTypes(
            "rafter",
            rafter_block::new,
            new Item.Properties());

    public static <T extends Block> DeferredBlock[] RegisterForAllWoodTypes(String name, Function<Block.Properties, T> blockFactory, Item.Properties properties)
    {
        Block[] woodTypes = new Block[] { Blocks.OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.MANGROVE_PLANKS, Blocks.CRIMSON_PLANKS, Blocks.WARPED_PLANKS, Blocks.CHERRY_PLANKS };
        DeferredBlock[] blocks = new DeferredBlock[woodTypes.length];

        int count = 0;
        for (Block wood: woodTypes)
        {
            String[] _temp = wood.getDescriptionId().toLowerCase().split("\\.");
            String plank = _temp[_temp.length - 1].replace("_", "").replace("planks", "");

            String _name = String.format("%s_%s", name, plank);
            DeferredBlock<T> block = BLOCKS.register(_name, () -> blockFactory.apply(BlockBehaviour.Properties.copy(wood)));
            ItemRegistry.ITEMS.register(_name, () -> new BlockItem(block.get(), properties));

            blocks[count++] = block;
        }

        return blocks;
    }

    public static <T extends Block> DeferredBlock<T> Register(String name, Supplier<T> supplier, Item.Properties properties)
    {
        DeferredBlock<T> block = BLOCKS.register(name, supplier);
        ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
}
