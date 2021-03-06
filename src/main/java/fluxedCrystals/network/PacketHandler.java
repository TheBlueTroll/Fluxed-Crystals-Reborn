package fluxedCrystals.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fluxedCrystals.network.message.MessageBiome;
import fluxedCrystals.network.message.MessageGemCutter;
import fluxedCrystals.network.message.MessageGemRefiner;
import fluxedCrystals.network.message.MessageSeedInfuser;
import fluxedCrystals.network.message.MessageSolarFluxSync;
import fluxedCrystals.network.message.MessageSyncMutation;
import fluxedCrystals.network.message.MessageSyncMutations;
import fluxedCrystals.network.message.MessageSyncSeed;
import fluxedCrystals.network.message.MessageSyncSeeds;
import fluxedCrystals.reference.Reference;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.LOWERCASE_MOD_ID);

	private static int id = 0;

	public static void init() {

		INSTANCE.registerMessage(MessageBiome.class, MessageBiome.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSeedInfuser.class, MessageSeedInfuser.class, id++, Side.SERVER);
		INSTANCE.registerMessage(MessageSolarFluxSync.class, MessageSolarFluxSync.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSyncSeed.class, MessageSyncSeed.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSyncSeeds.class, MessageSyncSeeds.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageGemRefiner.class, MessageGemRefiner.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageGemCutter.class, MessageGemCutter.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSyncMutation.class, MessageSyncMutation.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSyncMutations.class, MessageSyncMutations.class, id++, Side.CLIENT);
	}

}
