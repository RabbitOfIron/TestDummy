package boni.dummy.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import boni.dummy.EntityDummy;
import io.netty.buffer.ByteBuf;

public class SyncEquipmentMessage implements IMessage {

  private int entityID;
  private int slotId;
  private ItemStack itemstack;

  public SyncEquipmentMessage() {
  }

  public SyncEquipmentMessage(int entityId, int slotId, ItemStack itemstack) {
    this.entityID = entityId;
    this.slotId = slotId;
    this.itemstack = itemstack == null ? null : itemstack.copy();
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.entityID = buf.readInt();
    this.slotId = buf.readInt();
    //this.itemstack = null;

    // let's try it like this since identification fails with null for some reason
    //if(buf.readBoolean())
    this.itemstack = ByteBufUtils.readItemStack(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(entityID);
    buf.writeInt(slotId);
    //buf.writeBoolean(itemstack != null);
    //if(itemstack != null)
    ByteBufUtils.writeItemStack(buf, itemstack);
  }

  public static class MessageHandlerClient implements IMessageHandler<SyncEquipmentMessage, IMessage> {

    @Override
    public SyncEquipmentMessage onMessage(final SyncEquipmentMessage message, MessageContext ctx) {
      FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(new Runnable() {
        @Override
        public void run() {
          Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityID);
          if(entity != null && entity instanceof EntityDummy) {
            EntityEquipmentSlot slot = EntityEquipmentSlot.values()[message.slotId];
            entity.setItemStackToSlot(slot, message.itemstack);
          }
        }
      });
      return null;
    }
  }
}
