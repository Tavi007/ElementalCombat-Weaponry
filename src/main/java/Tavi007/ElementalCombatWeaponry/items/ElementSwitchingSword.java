package Tavi007.ElementalCombatWeaponry.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import Tavi007.ElementalCombat.api.AttackDataAPI;
import Tavi007.ElementalCombat.capabilities.attack.AttackLayer;
import Tavi007.ElementalCombat.config.ServerConfig;
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

public class ElementSwitchingSword extends SwordItem {

    private Set<String> elements = new HashSet<String>();
    private ResourceLocation location = new ResourceLocation(ElementalCombatWeaponry.MOD_ID, "element_switch");

    public ElementSwitchingSword(IItemTier tier, int attackDamageIn, float attackSpeedIn, Set<String> elements, Properties p_i48460_4_) {
        super(tier, attackDamageIn, attackSpeedIn, p_i48460_4_);
        if (elements.isEmpty()) {
            this.elements.add(ServerConfig.getDefaultElement());
        } else {
            this.elements = elements;
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        AttackLayer layer = AttackDataAPI.getLayer(stack, location);
        String nextElement = CollectionUtil.getNext(elements, layer.getElement(), true);
        if (nextElement != null) {
            layer.setElement(nextElement);
            AttackDataAPI.putLayer(stack, layer, location, playerIn);
            return ActionResult.success(playerIn.getItemInHand(handIn));
        } else {
            AttackDataAPI.deleteLayer(stack, location, playerIn);
            layer.setElement(elements.iterator().next());
            AttackDataAPI.putLayer(stack, layer, location, playerIn);
            return ActionResult.fail(playerIn.getItemInHand(handIn));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent("" + TextFormatting.GRAY + "Right-click to switch element" + TextFormatting.RESET));
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
