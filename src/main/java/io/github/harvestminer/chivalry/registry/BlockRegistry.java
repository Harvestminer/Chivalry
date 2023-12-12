package io.github.harvestminer.chivalry.registry;

import io.github.harvestminer.chivalry.Chivalry;
import io.github.harvestminer.chivalry.block.example_block;
import io.github.harvestminer.chivalry.block.table_block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockRegistry
{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Chivalry.MODID);

    //public static final DeferredBlock<Block>[] BEAMS = RegisterAllWoodTypes("beam", () -> new example_block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD)), new Item.Properties());
    public static final DeferredBlock<Block>[] TABLES = RegisterAllWoodTypes(
            "table",
            () -> new table_block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD)),
            new Item.Properties());

    public static <T extends Block> DeferredBlock<T>[] RegisterAllWoodTypes(String name, Supplier<T> supplier, Item.Properties properties)
    {
        String woodTypes[] = new String[] { "oak", "birch", "spruce", "jungle", "acacia", "darkoak", "mangrove", "crimson", "warped" };
        DeferredBlock<T>[] blocks = new DeferredBlock[woodTypes.length];

        int count = 0;
        for (String wood: woodTypes)
        {
            String _name = String.format("%s_%s",name, wood);
            DeferredBlock<T> block = BLOCKS.register(_name, supplier);
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
