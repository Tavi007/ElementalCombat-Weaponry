package Tavi007.ElementalCombat_Weaponry.items;

import java.util.List;

import javax.annotation.Nullable;

import Tavi007.ElementalCombat.api.BasePropertiesAPI;
import Tavi007.ElementalCombat.api.DefenseDataAPI;
import Tavi007.ElementalCombat.api.defense.DefenseLayer;
import Tavi007.ElementalCombat.util.ElementalCombatNBTHelper;
import Tavi007.ElementalCombat_Weaponry.ElementalCombatWeaponry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BiomeDependentArmor extends  ArmorItem {

	public BiomeDependentArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties p_i48534_3_) {
		super(materialIn, slot, p_i48534_3_);
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		if(world.getDayTime() % 20 == 0) {
			Biome biome = world.getBiome(player.getPosition());
			DefenseLayer layer = BasePropertiesAPI.getDefenseLayer(biome);
			DefenseDataAPI.putLayer(stack, layer, ElementalCombatWeaponry.ARMOR_RESOURCE_LOCATION);
		}
	}

	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		CompoundNBT nbt = stack.getTag();
		ElementalCombatNBTHelper.writeDefenseDataToNBT(nbt, DefenseDataAPI.get(stack));
		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
		stack.setTag(nbt);
		DefenseDataAPI.get(stack).set(ElementalCombatNBTHelper.readDefenseDataFromNBT(nbt));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent("" + TextFormatting.GRAY + "Elemental defense depends on current biome." + TextFormatting.RESET));
	}
}