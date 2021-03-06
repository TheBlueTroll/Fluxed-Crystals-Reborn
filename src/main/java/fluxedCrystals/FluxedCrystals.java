package fluxedCrystals;

import java.io.File;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLModIdMappingEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import fluxedCrystals.client.gui.GUIHandler;
import fluxedCrystals.command.CommandFC;
import fluxedCrystals.handler.ConfigurationHandler;
import fluxedCrystals.handler.RecipeHandler;
import fluxedCrystals.init.FCBlocks;
import fluxedCrystals.init.FCItems;
import fluxedCrystals.network.PacketHandler;
import fluxedCrystals.proxy.IProxy;
import fluxedCrystals.recipe.RecipeGemCutter;
import fluxedCrystals.recipe.RecipeGemRefiner;
import fluxedCrystals.recipe.RecipeRegistry;
import fluxedCrystals.recipe.RecipeSeedInfuser;
import fluxedCrystals.reference.Reference;
import fluxedCrystals.registry.MutationRegistry;
import fluxedCrystals.registry.Seed;
import fluxedCrystals.registry.SeedRegistry;
import fluxedCrystals.util.LogHelper;
import fluxedCrystals.util.OreDict;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, name = Reference.MOD_NAME, guiFactory = Reference.GUI_FACTORY_CLASS)
public class FluxedCrystals {

	public static final CreativeTabFluxedCrystals tab = new CreativeTabFluxedCrystals();
	public static File configDir = null;
	public static int crystalRenderID;
	public static int bigCubeID;
	@Mod.Instance(Reference.MOD_ID)
	public static FluxedCrystals instance;
	public static int seedInfuserRenderID;
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
	public static IProxy proxy;

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {

		event.registerServerCommand(new CommandFC());
	}

	// Force the client and server to have or not have this mod
	@NetworkCheckHandler()
	public boolean matchModVersions(Map<String, String> remoteVersions, Side side) {

		return remoteVersions.containsKey(Reference.MOD_ID) && Reference.VERSION.equals(remoteVersions.get(Reference.MOD_ID));
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		configDir = new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + File.separator + Reference.MOD_ID);
		ConfigurationHandler.init(new File(configDir.getAbsolutePath() + File.separator + Reference.MOD_ID + ".cfg"));
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
		OreDict.registerVanilla();
		PacketHandler.init();
		SeedRegistry.getInstance();
		SeedRegistry.getInstance().Load();
		MutationRegistry.getInstance();
		MutationRegistry.getInstance().Load();
		FCItems.preInit();
		FCBlocks.preInit();
		proxy.preInit();
		FMLInterModComms.sendMessage("Waila", "register", "fluxedCrystals.compat.waila.WailaCompat.load");
		LogHelper.info("Pre Initialization Complete!");
	}

	@Mod.EventHandler
	public void initialize(FMLInitializationEvent event) {

		FCItems.initialize();
		FCBlocks.initialize();
		proxy.initialize();
		proxy.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
		LogHelper.info("Initialization Complete!");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		FCItems.postInit();
		FCBlocks.postInit();
		RecipeHandler.postInit();
		proxy.postInit();
		for (int i : SeedRegistry.getInstance().keySet()) {
			Seed seed = SeedRegistry.getInstance().getSeedByID(i);
			if (seed.modRequired.equals("") || (!seed.modRequired.equals("") && Loader.isModLoaded(seed.modRequired))) {
				RecipeRegistry.registerSeedInfuserRecipe(seed.seedID, new RecipeSeedInfuser(new ItemStack(FCItems.universalSeed), seed.getIngredient(), new ItemStack(FCItems.seed, 1, seed.seedID), seed.ingredientAmount, seed.seedID));
				RecipeRegistry.registerGemCutterRecipe(seed.seedID, new RecipeGemCutter(new ItemStack(FCItems.shardRough, 1, seed.seedID), new ItemStack(FCItems.shardSmooth, 1, seed.seedID), 1, 1));
				if (seed.weightedDrop != null && !seed.weightedDrop.equals("")) {
					if (!(Block.getBlockFromName("minecraft:portal") == Block.getBlockFromItem(seed.getWeightedDrop().getItem()))) {
						RecipeRegistry.registerGemRefinerRecipe(seed.seedID, new RecipeGemRefiner(new ItemStack(FCItems.shardSmooth, 1, i), seed.getWeightedDrop(), seed.refinerAmount, 1));
					} else {
						RecipeRegistry.registerGemRefinerRecipe(seed.seedID, new RecipeGemRefiner(new ItemStack(FCItems.shardSmooth, 1, i), seed.getIngredient(), seed.refinerAmount, 1));
					}
				} else {
					RecipeRegistry.registerGemRefinerRecipe(seed.seedID, new RecipeGemRefiner(new ItemStack(FCItems.shardSmooth, 1, i), seed.getIngredient(), seed.refinerAmount, 1));
				}
			}
		}
		LogHelper.info("Recipe Loading Complete!!");
		LogHelper.info("Post Initialization Complete!");
	}

	@Mod.EventHandler
	public void onServerStopping(FMLServerStoppingEvent event) {

		SeedRegistry.getInstance().Save();
		MutationRegistry.getInstance().Save();
	}

//	@Mod.EventHandler
//	public void alias(FMLMissingMappingsEvent e) {
//		for (FMLMissingMappingsEvent.MissingMapping map : e.getAll()) {
//			if (map.name.startsWith("fluxedCrystals:") || map.name.startsWith("fluxedcrystals:")) {
//				if (map.type == GameRegistry.Type.BLOCK)
//					for (String key : FCBlocks.blockRegistry.keySet()) {
//						if (map.name.endsWith(key)) {
//							System.out.println(map.name + ":" + key);
//							map.remap(FCBlocks.blockRegistry.get(key));
//						}
//					}
//				System.out.println(map.name);
//				if (map.type == GameRegistry.Type.ITEM) {
//					for (String key : FCItems.itemRegistry.keySet()) {
//						if (map.name.endsWith(key)) {
//							System.out.println(map.name + ":" + key);
//							map.remap(FCItems.itemRegistry.get(key));
//						}
//					}
//				}
//
//			}
//		}
//	}

	@Mod.EventHandler
	public void remap(FMLModIdMappingEvent event) {

		// TODO Need to add the mutations
		for (int i : SeedRegistry.getInstance().keySet()) {
			Seed seed = SeedRegistry.getInstance().getSeedByID(i);
			if (seed.modRequired.equals("") || (!seed.modRequired.equals("") && Loader.isModLoaded(seed.modRequired))) {
				RecipeRegistry.registerSeedInfuserRecipe(seed.seedID, new RecipeSeedInfuser(new ItemStack(FCItems.universalSeed), seed.getIngredient(), new ItemStack(FCItems.seed, 1, seed.seedID), seed.ingredientAmount, seed.seedID));
				RecipeRegistry.registerGemCutterRecipe(seed.seedID, new RecipeGemCutter(new ItemStack(FCItems.shardRough, 1, seed.seedID), new ItemStack(FCItems.shardSmooth, 1, seed.seedID), 1, 1));
				if (seed.weightedDrop != null && !seed.weightedDrop.equals("")) {
					if (!(Block.getBlockFromName("minecraft:portal") == Block.getBlockFromItem(seed.getWeightedDrop().getItem()))) {
						RecipeRegistry.registerGemRefinerRecipe(seed.seedID, new RecipeGemRefiner(new ItemStack(FCItems.shardSmooth, 1, i), seed.getWeightedDrop(), seed.refinerAmount, 1));
					} else {
						RecipeRegistry.registerGemRefinerRecipe(seed.seedID, new RecipeGemRefiner(new ItemStack(FCItems.shardSmooth, 1, i), seed.getIngredient(), seed.refinerAmount, 1));
					}
				} else {
					RecipeRegistry.registerGemRefinerRecipe(seed.seedID, new RecipeGemRefiner(new ItemStack(FCItems.shardSmooth, 1, i), seed.getIngredient(), seed.refinerAmount, 1));
				}
			}
		}
		LogHelper.info("Remap Complete!");
	}
}
