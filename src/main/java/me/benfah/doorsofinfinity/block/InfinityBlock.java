package me.benfah.doorsofinfinity.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;

public class InfinityBlock extends Block
{

    public static final EnumProperty<Color> COLOR = EnumProperty.create("color", Color.class);

    public InfinityBlock(Properties settings)
    {
        super(settings);
        setDefaultState(getDefaultState().with(COLOR, Color.BLACK));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(COLOR);
    }

    public static enum Color implements IStringSerializable
    {
        BLACK("black"), WHITE("white");

        String name;

        Color(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return name;
        }
    }

}
