package me.benfah.doorsofinfinity.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public class InfinityBlock extends Block
{

    public static final EnumProperty<Color> COLOR = EnumProperty.of("color", Color.class);

    public InfinityBlock(Settings settings)
    {
        super(settings);
        setDefaultState(getDefaultState().with(COLOR, Color.BLACK));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        builder.add(COLOR);
    }

    public static enum Color implements StringIdentifiable
    {
        BLACK("black"), WHITE("white");

        String name;

        Color(String name)
        {
            this.name = name;
        }

        @Override
        public String asString()
        {
            return name;
        }
    }

}
