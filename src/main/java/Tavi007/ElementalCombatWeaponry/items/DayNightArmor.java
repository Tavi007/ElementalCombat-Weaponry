package Tavi007.ElementalCombatWeaponry.items;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import Tavi007.ElementalCombat.api.DefenseDataAPI;
import Tavi007.ElementalCombat.capabilities.defense.DefenseLayer;
import Tavi007.ElementalCombatWeaponry.ElementalCombatWeaponry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DayNightArmor extends  ArmorItem {
	
	public DayNightArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties p_i48534_3_) {
		super(materialIn, slot, p_i48534_3_);
	}
	
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		float time = (float) world.getDayTime();
		// time: 18000 = midnight; 6000 = noon; 12000 and 0 (or 24000) halfway points
		int lightFactor = Math.round((float) Math.sin(time/12000 * Math.PI) * 35) ;
		HashMap<String,Integer> elemFactors = new HashMap<String, Integer>();
		if(lightFactor>=0) {
			elemFactors.put("light", lightFactor);
			elemFactors.put("darkness", -lightFactor/2);
		}
		else {
			elemFactors.put("light", lightFactor/2);
			elemFactors.put("darkness", -lightFactor);
		}
		DefenseLayer layer = new DefenseLayer();
		layer.addElement(elemFactors);
		DefenseDataAPI.putLayer(stack, layer, ElementalCombatWeaponry.ARMOR_RESOURCE_LOCATION);
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
    	tooltip.add(new StringTextComponent("" + TextFormatting.GRAY + "Elemental defense depends on world time." + TextFormatting.RESET));
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
    	long time = entity.world.getDayTime();
    	if (time >= 1000 && time<11000) {
            return ElementalCombatWeaponry.MOD_ID + ":textures/models/armor/clock_day_layer_1.png";
    	}
    	else if (time >= 13000 && time<23000) {
            return ElementalCombatWeaponry.MOD_ID + ":textures/models/armor/clock_night_layer_1.png";
    	}
    	else {
            return ElementalCombatWeaponry.MOD_ID + ":textures/models/armor/clock_sunset_layer_1.png";
    	}
    }
}
