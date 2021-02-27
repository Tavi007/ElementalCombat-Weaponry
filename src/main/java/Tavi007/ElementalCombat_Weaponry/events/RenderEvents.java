package Tavi007.ElementalCombat.events;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import Tavi007.ElementalCombat.ElementalCombat;
import Tavi007.ElementalCombat.ElementalCombatAPI;
import Tavi007.ElementalCombat.capabilities.attack.AttackData;
import Tavi007.ElementalCombat.capabilities.defense.DefenseData;
import Tavi007.ElementalCombat.capabilities.immersion.ImmersionData;
import Tavi007.ElementalCombat.capabilities.immersion.ImmersionDataCapability;
import Tavi007.ElementalCombat.config.ClientConfig;
import Tavi007.ElementalCombat.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = ElementalCombat.MOD_ID, value = Dist.CLIENT, bus = Bus.FORGE)
public class RenderEvents {

	@SubscribeEvent
	public static void onRenderLivingEventPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
		LivingEntity entityIn = event.getEntity();
		ImmersionData data = (ImmersionData) entityIn.getCapability(ImmersionDataCapability.IMMERSION_DATA_CAPABILITY, null).orElse(new ImmersionData());
		if(entityIn.hurtTime > 0) {
			if (data.disableFlag) {
				data.setHurtTime(entityIn.hurtTime);
				entityIn.hurtTime = 0; //desync client and server hurtTime.
			}
		}
		else {
			data.disableFlag = false;
		}
	}

	@SubscribeEvent
	public static void onRenderLivingEventPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
		LivingEntity entityIn = event.getEntity();
		ImmersionData data = (ImmersionData) entityIn.getCapability(ImmersionDataCapability.IMMERSION_DATA_CAPABILITY, null).orElse(new ImmersionData());
		if (data.disableFlag && data.getHurtTime() > 0) {
			entityIn.hurtTime = data.getHurtTime();
			data.setHurtTime(0);
		}
	}

	@SubscribeEvent
	public static void onEntityViewRenderEvent(CameraSetup event) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null) {
			if (mc.player.hurtTime > 0) {
				ImmersionData data = (ImmersionData) mc.player.getCapability(ImmersionDataCapability.IMMERSION_DATA_CAPABILITY, null).orElse(new ImmersionData());
				if(data.disableFlag) {
					// Use the same calculation as in GameRenderer#hurtCameraEffect.
					float f = (float) (mc.player.hurtTime - event.getRenderPartialTicks());
					f = f / (float) mc.player.maxHurtTime;
					f = MathHelper.sin(f * f * f * f * (float)Math.PI);
					event.setRoll(f * 14.0F); // counter acts the screen shake. Only the hand is moving now.
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlaySoundEvent(PlaySoundEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null) {
			if (mc.player.hurtTime == mc.player.maxHurtTime) {
				ImmersionData data = (ImmersionData) mc.player.getCapability(ImmersionDataCapability.IMMERSION_DATA_CAPABILITY, null).orElse(new ImmersionData());
				if(data.disableFlag) {
					//What if other mods implements their own version of an hurt sound?
					//Also what if the on_fire sound gets disabled, even so I still took fire damage?
					if( event.getSound().getSoundLocation().equals(SoundEvents.ENTITY_PLAYER_HURT.getRegistryName()) || 
						event.getSound().getSoundLocation().equals(SoundEvents.ENTITY_PLAYER_HURT_DROWN.getRegistryName()) ||
						event.getSound().getSoundLocation().equals(SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE.getRegistryName()) ||
						event.getSound().getSoundLocation().equals(SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH.getRegistryName()) ) {
						event.setResult(null); 
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void addTooltipInformation(ItemTooltipEvent event) {
		ItemStack item = event.getItemStack();
		List<ITextComponent> toolTip = event.getToolTip();
		if (item != null) {
			//attack
			AttackData atckData = new AttackData(ElementalCombatAPI.getAttackData(item));
			if(!atckData.isEmpty()) {
				toolTip.addAll(RenderHelper.getDisplayText(atckData));
			}

			//defense
			DefenseData defData = new DefenseData(ElementalCombatAPI.getDefenseData(item));
			if(!defData.isEmpty()) {
				toolTip.addAll(RenderHelper.getDisplayText(defData));
			}
		}
	}

	static int ticks=0;
	static int counter=0;

	@SubscribeEvent
	public static void displayData(RenderGameOverlayEvent.Post event) {
		if(event.getType().equals(RenderGameOverlayEvent.ElementType.HOTBAR)) {
			if(ClientConfig.isHUDEnabled()) {
				// see Screen#renderToolTips in client.gui.screen
				Minecraft mc = Minecraft.getInstance();
				if(mc.player != null) {
					List<ITextComponent> list = new ArrayList<ITextComponent>();

					AttackData atckData = ElementalCombatAPI.getAttackDataWithActiveItem(mc.player);
					list.addAll(RenderHelper.getDisplayText(atckData));

					DefenseData defData = ElementalCombatAPI.getDefenseData(mc.player);
					if(!defData.isEmpty()) {
						if(ClientConfig.iterateDefense()) {
							ticks++;
							if(ticks>1.5*ClientConfig.iterationSpeed()) { //this event triggers slightly more often than the waila tooltip 
								ticks = 0;
								counter++;
							}
							if(counter>100) {
								counter = 0;
							}
							list.addAll(RenderHelper.getIteratingDisplayText(defData, counter));
						}
						else {
							list.addAll(RenderHelper.getDisplayText(defData));
						}
					}

					if (!list.isEmpty()) {
						MatrixStack matrixStack = event.getMatrixStack();

						matrixStack.push();
						float scale = (float) ClientConfig.scale();
						matrixStack.scale(scale, scale, scale);

						List<? extends IReorderingProcessor> orderedList = Lists.transform(list, ITextComponent::func_241878_f);
						// computes the width of the widest line.
						int listWidth = 0;
						for(IReorderingProcessor ireorderingprocessor : orderedList) {
							int textWidth = mc.fontRenderer.func_243245_a(ireorderingprocessor);
							listWidth = Math.max(textWidth, listWidth);
						}

						// computes the height of the list
						int listHeight = 8;
						if (orderedList.size() > 1) {
							listHeight += 2 + (orderedList.size() - 1) * (mc.fontRenderer.FONT_HEIGHT+1);
						}

						// moves the coords so the text and box appear correct
						int posX = 12;
						int posY = 12;
						if(!ClientConfig.isTop()) {
							int screenHeight = (int) (event.getWindow().getScaledHeight()/scale);
							posY = Math.max(12, screenHeight - listHeight - 12);
						}
						if(!ClientConfig.isLeft()) {
							int screenWidth = (int) (event.getWindow().getScaledWidth()/scale);
							posX = Math.max(12, screenWidth - listWidth - 12);
						}
						
						Tessellator tessellator = Tessellator.getInstance();
						BufferBuilder bufferbuilder = tessellator.getBuffer();
						bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
						Matrix4f matrix4f = matrixStack.getLast().getMatrix();
						// draw background box
						func_238462_a_(matrix4f, bufferbuilder, posX - 3, posY - 4, posX + listWidth + 3, posY - 3, 400, -267386864, -267386864);
						func_238462_a_(matrix4f, bufferbuilder, posX - 3, posY + listHeight + 3, posX + listWidth + 3, posY + listHeight + 4, 400, -267386864, -267386864);
						func_238462_a_(matrix4f, bufferbuilder, posX - 3, posY - 3, posX + listWidth + 3, posY + listHeight + 3, 400, -267386864, -267386864);
						func_238462_a_(matrix4f, bufferbuilder, posX - 4, posY - 3, posX - 3, posY + listHeight + 3, 400, -267386864, -267386864);
						func_238462_a_(matrix4f, bufferbuilder, posX + listWidth + 3, posY - 3, posX + listWidth + 4, posY + listHeight + 3, 400, -267386864, -267386864);
						func_238462_a_(matrix4f, bufferbuilder, posX - 3, posY - 3 + 1, posX - 3 + 1, posY + listHeight + 3 - 1, 400, 1347420415, 1344798847);
						func_238462_a_(matrix4f, bufferbuilder, posX + listWidth + 2, posY - 3 + 1, posX + listWidth + 3, posY + listHeight + 3 - 1, 400, 1347420415, 1344798847);
						func_238462_a_(matrix4f, bufferbuilder, posX - 3, posY - 3, posX + listWidth + 3, posY - 3 + 1, 400, 1347420415, 1347420415);
						func_238462_a_(matrix4f, bufferbuilder, posX - 3, posY + listHeight + 2, posX + listWidth + 3, posY + listHeight + 3, 400, 1344798847, 1344798847);
						RenderSystem.enableDepthTest();
						RenderSystem.disableTexture();
						RenderSystem.enableBlend();
						RenderSystem.defaultBlendFunc();
						RenderSystem.shadeModel(7425);
						bufferbuilder.finishDrawing();
						WorldVertexBufferUploader.draw(bufferbuilder);
						RenderSystem.shadeModel(7424);
						RenderSystem.disableBlend();
						RenderSystem.enableTexture();
						IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
						matrixStack.translate(0.0D, 0.0D, 400.0D);

						// write the list on top of the background
						for(int i = 0; i < orderedList.size(); ++i) {
							IReorderingProcessor ireorderingprocessor1 = orderedList.get(i);
							if (ireorderingprocessor1 != null) {
								mc.fontRenderer.func_238416_a_(ireorderingprocessor1, (float)posX, (float)posY, -1, ClientConfig.textShadow(), matrix4f, irendertypebuffer$impl, false, 0, 15728880);
							}
							//next line
							posY += 10;
						}
						irendertypebuffer$impl.finish();
						matrixStack.pop();
					}

				}
			}
		}
	}

	//copied from Screen
	protected static void func_238462_a_(Matrix4f p_238462_0_, BufferBuilder p_238462_1_, int p_238462_2_, int p_238462_3_, int p_238462_4_, int p_238462_5_, int p_238462_6_, int p_238462_7_, int p_238462_8_) {
		float f = (float)(p_238462_7_ >> 24 & 255) / 255.0F;
		float f1 = (float)(p_238462_7_ >> 16 & 255) / 255.0F;
		float f2 = (float)(p_238462_7_ >> 8 & 255) / 255.0F;
		float f3 = (float)(p_238462_7_ & 255) / 255.0F;
		float f4 = (float)(p_238462_8_ >> 24 & 255) / 255.0F;
		float f5 = (float)(p_238462_8_ >> 16 & 255) / 255.0F;
		float f6 = (float)(p_238462_8_ >> 8 & 255) / 255.0F;
		float f7 = (float)(p_238462_8_ & 255) / 255.0F;
		p_238462_1_.pos(p_238462_0_, (float)p_238462_4_, (float)p_238462_3_, (float)p_238462_6_).color(f1, f2, f3, f).endVertex();
		p_238462_1_.pos(p_238462_0_, (float)p_238462_2_, (float)p_238462_3_, (float)p_238462_6_).color(f1, f2, f3, f).endVertex();
		p_238462_1_.pos(p_238462_0_, (float)p_238462_2_, (float)p_238462_5_, (float)p_238462_6_).color(f5, f6, f7, f4).endVertex();
		p_238462_1_.pos(p_238462_0_, (float)p_238462_4_, (float)p_238462_5_, (float)p_238462_6_).color(f5, f6, f7, f4).endVertex();
	}
}
