package Tavi007.ElementalCombatWeaponry.items;

import java.util.List;

import javax.annotation.Nullable;

import Tavi007.ElementalCombat.api.DefenseDataAPI;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MirrorArmor extends ArmorItem {

    public MirrorArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        DefenseDataAPI.writeToNBT(nbt, stack);
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        DefenseDataAPI.readFromNBT(nbt, stack);
        stack.setTag(nbt);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent("" + TextFormatting.GRAY + "Elemental defense depends on last damage taken." + TextFormatting.RESET));
    }

}
