package mekanism.common.asm;

import codechicken.core.launch.DepLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public class LoadingHook implements IFMLLoadingPlugin
{
	public LoadingHook()
	{
		DepLoader.load();
	}

	public String[] getLibraryRequestClass()
	{
		return null;
	}

	public String[] getASMTransformerClass()
	{
		return null;
	}

	public String getModContainerClass()
	{
		return null;
	}

	public String getSetupClass()
	{
		return null;
	}

	public void injectData(Map<String, Object> data)
	{

	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}
