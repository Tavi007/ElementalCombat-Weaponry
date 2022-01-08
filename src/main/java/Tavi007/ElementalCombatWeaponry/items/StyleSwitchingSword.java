package Tavi007.ElementalCombatWeaponry.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import Tavi007.ElementalCombat.api.AttackDataAPI;
import Tavi007.ElementalCombat.capabilities.attack.AttackLayer;
import Tavi007.ElementalCombatWeaponry.ElementalCombatWeaponry;
import Tavi007.ElementalCombatWeaponry.util.CollectionUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StyleSwitchingSword extends SwordItem {

    private Set<String> styles = new HashSet<String>();
    private ResourceLocation location = new ResourceLocation(ElementalCombatWeaponry.MOD_ID, "style_switch");

    public StyleSwitchingSword(IItemTier tier, int attackDamageIn, float attackSpeedIn, Set<String> styles, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
        this.styles = styles;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        AttackLayer layer = AttackDataAPI.getLayer(stack, location);
        String nextStyle = CollectionUtil.getNext(styles, layer.getStyle(), true);
        if (nextStyle != null) {
            layer.setStyle(nextStyle);
            AttackDataAPI.putLayer(stack, layer, location, playerIn);
            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
        } else {
            AttackDataAPI.deleteLayer(stack, location, playerIn);
            layer.setStyle(styles.iterator().next());
            AttackDataAPI.putLayer(stack, layer, location, playerIn);
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
        AttackDataAPI.writeToNBT(nbt, stack);
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        AttackDataAPI.readFromNBT(nbt, stack);
        stack.setTag(nbt);
    }
}
