package fluxedCrystals.client.gui.slot;

import fluxedCrystals.FluxedCrystals;
import fluxedCrystals.init.FCItems;
import fluxedCrystals.util.StringUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatisticsFile;

public class SlotInformation extends Slot {

	public SlotInformation(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
		super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
	}

	public int getSlotStackLimit() {
		return 1;
	}

	public boolean isItemValid(ItemStack stack) {

		return hasInformation(stack);

	}

	private boolean hasInformation(ItemStack stack) {
		if (StringUtils.localize("fc." + stack.getUnlocalizedName() + ".information", false).isEmpty()) {
			return true;
		}
		return false;
	}

}
