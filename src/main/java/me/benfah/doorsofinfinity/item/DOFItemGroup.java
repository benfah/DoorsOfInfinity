package me.benfah.doorsofinfinity.item;

import me.benfah.doorsofinfinity.init.DOFItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class DOFItemGroup extends ItemGroup
{
    public DOFItemGroup(String label)
    {
        super(label);
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(DOFItems.INFINITY_DOOR.get());
    }
}
