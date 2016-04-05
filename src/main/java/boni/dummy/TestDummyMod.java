package boni.dummy;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;


@Mod(modid = TestDummyMod.MODID, version = TestDummyMod.VERSION, name = "MmmMmmMmmMmm", dependencies = "required-after:Forge@[12.15,)", acceptedMinecraftVersions = "[1.9,1.9.999]")
public class TestDummyMod {

  public static final String MODID = "testdummy";
  public static final String VERSION = "1.10";

  @Mod.Instance
  public static TestDummyMod instance;

  @SidedProxy(clientSide = "boni.dummy.ClientProxy", serverSide = "boni.dummy.CommonProxy")
  public static CommonProxy proxy;

  public static Item itemDummy = new ItemDummy();

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    GameRegistry.register(itemDummy);

    EntityRegistry.registerModEntity(EntityDummy.class, "Dummy", 0, TestDummyMod.instance, 32, 10, false);
    EntityRegistry.registerModEntity(EntityFloatingNumber.class, "FloatingNumber", 1, TestDummyMod.instance, 32, 1, false);
    EntityRegistry.registerModEntity(EntityDpsFloatingNumber.class, "FloatingNumberDPS", 2, TestDummyMod.instance, 32, 1, false);

    proxy.preinit();
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    GameRegistry.addRecipe(new ShapedOreRecipe(itemDummy,
                                               " B ",
                                               "HWH",
                                               " P ",
                                               'B', Item.getItemFromBlock(Blocks.hay_block),
                                               'H', Items.wheat,
                                               'W', new ItemStack(Blocks.wool, 0, OreDictionary.WILDCARD_VALUE),
                                               'P', "plankWood"));

    proxy.init();
  }

  @Mod.EventHandler
  public void missing(FMLMissingMappingsEvent event) {
    for(FMLMissingMappingsEvent.MissingMapping missing : event.get()) {
      if(missing.name.equals("testdummy:Dummy")) {
        missing.remap(itemDummy);
      }
    }
  }
}
