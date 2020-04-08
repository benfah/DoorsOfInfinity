package me.benfah.doorsofinfinity.command;

import com.mojang.brigadier.CommandDispatcher;

import me.benfah.doorsofinfinity.init.DOFDimensions;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.command.arguments.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class TestCommand
{

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		dispatcher.register(CommandManager.literal("infinity")
				.then(CommandManager.argument("location", Vec3ArgumentType.vec3()).executes((command) ->
				{
					Vec3d pos = Vec3ArgumentType.getPosArgument(command, "location").toAbsolutePos(command.getSource());
					ServerPlayerEntity p = command.getSource().getPlayer();
					FabricDimensions.teleport(p, DOFDimensions.INFINITY_DIM);
					p.teleport(pos.x, pos.y, pos.z);
					return 1;
				})));
	}
}
