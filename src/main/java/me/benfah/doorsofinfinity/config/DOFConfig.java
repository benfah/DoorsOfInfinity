package me.benfah.doorsofinfinity.config;

import me.benfah.doorsofinfinity.DOFMod;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = DOFMod.MOD_ID)
public class DOFConfig implements ConfigData
{
    public boolean requireDoorBorder = true;

    public int dimensionSize = 11;

    public static DOFConfig getInstance()
    {
        return AutoConfig.getConfigHolder(DOFConfig.class).getConfig();
    }

}
