package Tavi007.ElementalCombat_Weaponry.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import Tavi007.ElementalCombat.api.AttackDataAPI;
import Tavi007.ElementalCombat.api.attack.AttackData;
import Tavi007.ElementalCombat.config.ServerConfig;
import Tavi007.ElementalCombat.util.ElementalCombatNBTHelper;
import Tavi007.ElementalCombat_Weaponry.util.CollectionUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StyleSwitchingSword extends SwordItem {

	private Set<String> styles = new HashSet<String>();
	
	public StyleSwitchingSword(IItemTier tier, int attackDamageIn, float attackSpeedIn, Set<String> styles, Properties builderIn) {
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
		this.styles = styles;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		AttackData atckData = AttackDataAPI.get(stack);
		String nextStyle = CollectionUtil.getNext(styles, atckData.getStyle(), true);
		if(styles != null) {
			atckData.setStyle(nextStyle);
			return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
		} else {
			atckData.setElement(ServerConfig.getDefaultStyle());
			return ActionResult.resultFail(playerIn.getHeldItem(handIn));
		}
	}

	@OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    	tooltip.add(new StringTextComponent("" + TextFormatting.GRAY + "Right-click to switch style" + TextFormatting.RESET));
    }

    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        ElementalCombatNBTHelper.writeAttackDataToNBT(nbt, AttackDataAPI.get(stack));
        return nbt;
    }

    @Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        stack.setTag(nbt);
        AttackDataAPI.get(stack).set(ElementalCombatNBTHelper.readAttackDataFromNBT(nbt));
    }
}
