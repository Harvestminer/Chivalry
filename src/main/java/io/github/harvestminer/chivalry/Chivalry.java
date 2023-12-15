package io.github.harvestminer.chivalry;

import com.mojang.logging.LogUtils;
import io.github.harvestminer.chivalry.registry.BlockRegistry;
import io.github.harvestminer.chivalry.registry.ItemRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(Chivalry.MODID)
public class Chivalry
{
    public static final String MODID = "chivalry";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Chivalry(IEventBus modEventBus)
    {
        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
