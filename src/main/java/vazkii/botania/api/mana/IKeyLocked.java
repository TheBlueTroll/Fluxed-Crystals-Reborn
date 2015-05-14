/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [Jul 11, 2014, 4:29:32 PM (GMT)]
 */
package vazkii.botania.api.mana;

/**
 * A TileEntity that implements this interface has an IO key lock. This
 * interface defines an input and output key.<br><br>
 * <p/>
 * A Spreader can only shoot mana into a IKeyLocked interfaced TE if the Input
 * key of the TE is equal to the Output key of the Spreader.<br><br>
 * <p/>
 * A Spreader can only pull mana from a IKeyLocked interfaced IManaPool TE if the
 * Output key of the IManaPool is equal to the Input key of the Spreader.
 */
public interface IKeyLocked {

	public String getInputKey();

	public String getOutputKey();

}
