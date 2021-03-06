package fluxedCrystals;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fluxedCrystals.init.FCItems;
import fluxedCrystals.reference.Reference;
import fluxedCrystals.registry.SeedRegistry;
import fluxedCrystals.util.TimeTracker;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import java.util.*;

public class CreativeTabFluxedCrystals extends CreativeTabs {

	private int iconIndex = -1;

	private TimeTracker iconTracker = new TimeTracker();

	public CreativeTabFluxedCrystals() {

		super(Reference.MOD_NAME);
//		setNoTitle();
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}

	private void updateIcon() {

		World var1 = FluxedCrystals.proxy.getClientWorld();

		if (FluxedCrystals.proxy.isClient() && this.iconTracker.hasDelayPassed(var1, 80)) {

			Random random = new Random();

			List<Integer> keys = new ArrayList<Integer>(SeedRegistry.getInstance().keySet());

			iconIndex = keys.get(random.nextInt(keys.size()));

		}

	}

	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {

		updateIcon();

		if (iconIndex != -1) {

			return new ItemStack(FCItems.seed, 1, iconIndex);

		} else {

			return new ItemStack(FCItems.universalSeed, 1);

		}

	}

	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {

		return this.getIconItemStack().getItem();

	}

	@Override
	public int getSearchbarWidth() {
		return 85;
	}

//	@Override
//	public String getBackgroundImageName() {
//		return "fluxedcrystals.png";
//	}

}
