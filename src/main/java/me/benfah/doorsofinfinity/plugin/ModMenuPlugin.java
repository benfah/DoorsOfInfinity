package me.benfah.doorsofinfinity.plugin;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.config.DOFConfig;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;

public class ModMenuPlugin implements ModMenuApi
{
    @Override
    public String getModId()
    {
        return DOFMod.MOD_ID;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> AutoConfig.getConfigScreen(DOFConfig.class, parent).get();
    }
}
