package me.benfah.doorsofinfinity.config;

import com.qouteall.hiding_in_the_bushes.ConfigClient;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class DOFConfig
{
    public static DOFConfig INSTANCE;
    public static ForgeConfigSpec spec;

    public final ForgeConfigSpec.IntValue dimensionSize;
    public final ForgeConfigSpec.IntValue maxUpgrades;
    public final ForgeConfigSpec.BooleanValue requireDoorBorder;

    public DOFConfig(ForgeConfigSpec.Builder builder)
    {
        dimensionSize = (ForgeConfigSpec.IntValue) builder.comment("Defines the inner dimension size.").defineInRange("dimensionSize",11, 0, 150);
        maxUpgrades = (ForgeConfigSpec.IntValue) builder.comment("Defines the maximum amount of upgrades a door can hold.").defineInRange("maxUpgrades",8, 0, 20);
        requireDoorBorder = builder.comment("Defines if blocks of infinity have to be used as border stones.").define("requireDoorBorder", true);

    }

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, spec);
    }

    static {
        Pair<DOFConfig, ForgeConfigSpec> pair =
                new ForgeConfigSpec.Builder().configure(DOFConfig::new);
        INSTANCE = pair.getKey();
        spec = pair.getValue();
    }

    public static DOFConfig getInstance()
    {
        return INSTANCE;
    }
}
