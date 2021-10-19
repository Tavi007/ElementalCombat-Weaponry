package Tavi007.ElementalCombatWeaponry.items;

import java.util.List;

import javax.annotation.Nullable;

import Tavi007.ElementalCombat.api.AttackDataAPI;
import Tavi007.ElementalCombat.capabilities.attack.AttackLayer;
import Tavi007.ElementalCombatWeaponry.ElementalCombatWeaponry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CopyAttackElementItem extends TieredItem {

	private ResourceLocation location = new ResourceLocation(ElementalCombatWeaponry.MOD_ID, "copy_staff");
	
	public CopyAttackElementItem(IItemTier tier, Properties builderIn) {
		super(tier, builderIn);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if(attacker.isCrouching()) {
			AttackDataAPI.deleteLayer(target, location);
		} else {
			AttackLayer layer = new AttackLayer();
			String element = AttackDataAPI.getFullDataAsLayer(stack).getElement();
			layer.setElement(element);
			AttackDataAPI.putLayer(target, layer, location);
		}
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent("" + TextFormatting.GRAY + "Left-Click to change elemental Attack of any mob." + TextFormatting.RESET));
		tooltip.add(new StringTextComponent("" + TextFormatting.GRAY + "Shift Left-Click to revert back to base value." + TextFormatting.RESET));
	}

}
