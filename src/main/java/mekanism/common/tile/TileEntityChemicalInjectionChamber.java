package mekanism.common.tile;

import java.util.Map;

import mekanism.api.EnumColor;
import mekanism.api.MekanismConfig.usage;
import mekanism.api.gas.*;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.MekanismBlocks;
import mekanism.common.SideData;
import mekanism.common.block.BlockMachine.MachineType;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.machines.InjectionRecipe;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityChemicalInjectionChamber extends TileEntityAdvancedElectricMachine<InjectionRecipe>
{
	public TileEntityChemicalInjectionChamber()
	{
		super("injection", "ChemicalInjectionChamber", usage.chemicalInjectionChamberUsage, 1, 200, MachineType.CHEMICAL_INJECTION_CHAMBER.baseEnergy);
		
		configComponent.addSupported(TransmissionType.GAS);
		configComponent.addOutput(TransmissionType.GAS, new SideData("None", EnumColor.GREY, InventoryUtils.EMPTY));
		configComponent.addOutput(TransmissionType.GAS, new SideData("Gas", EnumColor.DARK_RED, new int[] {0}));
		configComponent.fillConfig(TransmissionType.GAS, 1);
		configComponent.setCanEject(TransmissionType.GAS, false);
	}

	@Override
	public Map getRecipes()
	{
		return Recipe.CHEMICAL_INJECTION_CHAMBER.get();
	}

	@Override
	public GasStack getItemGas(ItemStack itemstack)
	{
		if (GasifyableItems.getGasFromItem(itemstack) != null) {
			//System.out.println(GasifyableItems.getGasFromItem(itemstack).getGas().getUnlocalizedName());
			return GasifyableItems.getGasFromItem(itemstack);
		}
		else
		if(Block.getBlockFromItem(itemstack.getItem()) == MekanismBlocks.GasTank && ((IGasItem)itemstack.getItem()).getGas(itemstack) != null &&
				isValidGas(((IGasItem)itemstack.getItem()).getGas(itemstack).getGas())) return new GasStack(((IGasItem)itemstack.getItem()).getGas(itemstack).getGas(), 1);

		return null;
	}

	@Override
	public int receiveGas(ForgeDirection side, GasStack stack, boolean doTransfer)
	{
		if(canReceiveGas(side, stack.getGas()))
		{
			return gasTank.receive(stack, doTransfer);
		}

		return 0;
	}

	@Override
	public boolean canReceiveGas(ForgeDirection side, Gas type)
	{
		if(configComponent.getOutput(TransmissionType.GAS, side.ordinal(), facing).hasSlot(0))
		{
			return isValidGas(type);
		}
		
		return false;
	}

	@Override
	public void handleSecondaryFuel()
	{
		if(inventory[1] != null && gasTank.getNeeded() > 0 && inventory[1].getItem() instanceof IGasItem)
		{
			GasStack gas = ((IGasItem)inventory[1].getItem()).getGas(inventory[1]);

			if(gas != null && isValidGas(gas.getGas()))
			{
				GasStack removed = GasTransmission.removeGas(inventory[1], gasTank.getGasType(), gasTank.getNeeded());
				gasTank.receive(removed, true);
			}

			return;
		}

		super.handleSecondaryFuel();
	}

	@Override
	public boolean canTubeConnect(ForgeDirection side)
	{
		return configComponent.getOutput(TransmissionType.GAS, side.ordinal(), facing).hasSlot(0);
	}

	@Override
	public boolean isValidGas(Gas gas)
	{

		return GasifyableItems.isGasValidGasifyable(gas);

	}

	@Override
	public boolean upgradeableSecondaryEfficiency()
	{
		return true;
	}

	@Override
	public boolean useStatisticalMechanics()
	{
		return true;
	}
}
