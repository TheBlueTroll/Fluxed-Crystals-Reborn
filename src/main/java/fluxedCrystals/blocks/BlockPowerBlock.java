package fluxedCrystals.blocks;

import fluxedCrystals.reference.Names;
import fluxedCrystals.reference.Reference;
import fluxedCrystals.tileEntity.TileEntityPowerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockPowerBlock extends Block  implements ITileEntityProvider
{

	public BlockPowerBlock() {

		super(Material.grass);
		this.setHardness(1.0F);
		this.setHarvestLevel("shovel", 1);
		this.setBlockName(Reference.LOWERCASE_MOD_ID + "." + Names.Blocks.POWEREDSOIL);
		this.setBlockTextureName(Reference.LOWERCASE_MOD_ID + ":" + Names.Blocks.POWEREDSOIL);
	}

	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
		stack.add(new ItemStack(this));
		return stack;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityPowerBlock();
	}

}
