package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.item.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DOFItems
{

	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, DOFMod.MOD_ID);
	public static ItemGroup DOF_GROUP = new DOFItemGroup("doorsofinfinity");

	public static RegistryObject<InfinityDoorItem> INFINITY_DOOR = ITEMS.register("infinity_door", () -> new InfinityDoorItem(DOFBlocks.INFINITY_DOOR.get(), new Item.Properties().group(DOF_GROUP)));

	public static RegistryObject<Item> BLOCK_OF_INFINITY = ITEMS.register("block_of_infinity", () -> new BlockItem(DOFBlocks.BLOCK_OF_INFINITY.get(), new Item.Properties().group(DOF_GROUP)));

	public static RegistryObject<Item> PHOTON_TRANSMITTER = ITEMS.register("photon_transmitter", () -> new BlockItem(DOFBlocks.PHOTON_TRANSMITTER.get(), new Item.Properties().group(DOF_GROUP)));
	public static RegistryObject<Item> PHOTON_LINK = ITEMS.register("photon_link", () -> new PhotonLinkItem(new Item.Properties().group(DOF_GROUP).maxStackSize(1)));

	public static RegistryObject<Item> DIMENSIONAL_SHARD = ITEMS.register("dimensional_shard", () -> new DimensionalShardItem(new Item.Properties().group(DOF_GROUP).maxStackSize(16)));

	public static DeferredRegister<?> getRegister()
	{
		return ITEMS;
	}

}
