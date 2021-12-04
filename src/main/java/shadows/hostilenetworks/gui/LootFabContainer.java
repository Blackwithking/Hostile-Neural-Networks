package shadows.hostilenetworks.gui;

import java.util.function.Predicate;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;
import shadows.hostilenetworks.Hostile;
import shadows.hostilenetworks.data.DataModel;
import shadows.hostilenetworks.item.MobPredictionItem;
import shadows.hostilenetworks.tile.LootFabTileEntity;
import shadows.hostilenetworks.tile.LootFabTileEntity.FabItemHandler;

public class LootFabContainer extends Container {

	protected final BlockPos pos;
	protected final World level;
	protected final LootFabTileEntity tile;

	public LootFabContainer(int id, PlayerInventory pInv, BlockPos pos) {
		super(Hostile.Containers.LOOT_FABRICATOR, id);
		this.pos = pos;
		this.level = pInv.player.level;
		this.tile = (LootFabTileEntity) level.getBlockEntity(pos);
		FabItemHandler inv = tile.getInventory();
		addSlot(new FilteredSlot(inv, 0, 79, 62, s -> s.getItem() == Hostile.Items.PREDICTION));

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				addSlot(new FilteredSlot(inv, 1 + y * 4 + x, 100 + x * 18, 7 + y * 18, s -> false));
			}
		}

		for (int row = 0; row < 9; row++) {
			addSlot(new Slot(pInv, row, 8 + row * 18, 154));
		}

		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				addSlot(new Slot(pInv, column + row * 9 + 9, 8 + column * 18, 96 + row * 18));
			}
		}

		this.addDataSlots(tile.getRefHolder());
	}

	@Override
	public boolean stillValid(PlayerEntity pPlayer) {
		return pPlayer.level.getBlockState(this.pos).getBlock() == Hostile.Blocks.LOOT_FABRICATOR;
	}

	@Override
	public boolean clickMenuButton(PlayerEntity pPlayer, int pId) {
		DataModel model = MobPredictionItem.getStoredModel(this.getSlot(0).getItem());
		if (model == null) return false;
		if (pId >= model.getFabDrops().size()) return false;
		this.tile.setSelection(model, pId);
		return true;
	}

	public int getEnergyStored() {
		return this.tile.getEnergyStored();
	}

	public int getRuntime() {
		return this.tile.getRuntime();
	}

	protected class FilteredSlot extends SlotItemHandler {

		protected final Predicate<ItemStack> filter;
		protected final int index;

		public FilteredSlot(FabItemHandler handler, int index, int x, int y, Predicate<ItemStack> filter) {
			super(handler, index, x, y);
			this.filter = filter;
			this.index = index;
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return this.filter.test(stack);
		}

		@Override
		public int getMaxStackSize() {
			return 64;
		}

		@Override
		public boolean mayPickup(PlayerEntity playerIn) {
			return true;
		}

		@Override
		public ItemStack remove(int amount) {
			return ((FabItemHandler) this.getItemHandler()).extractItemInternal(this.index, amount, false);
		}

	}

}