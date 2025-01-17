//
// Created by BONNe
// Copyright - 2019
//


package world.bentobox.controlpanel.panels;


import org.bukkit.Material;
import java.util.Comparator;

import world.bentobox.bentobox.api.panels.PanelItem;
import world.bentobox.bentobox.api.panels.builders.PanelBuilder;
import world.bentobox.bentobox.api.panels.builders.PanelItemBuilder;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.controlpanel.ControlPanelAddon;
import world.bentobox.controlpanel.database.objects.ControlPanelObject;
import world.bentobox.controlpanel.database.objects.ControlPanelObject.ControlPanelButton;


/**
 * This class generates Control Panel using given ControlPanelObject.
 */
public class ControlPanelGenerator
{
	/**
	 * Default constructor.
	 * @param addon ControlPanelAddon instance.
	 * @param user User who opens panel.
	 * @param controlPanel ControlPanelSettings that must be constructed
	 * @param topLabel Main command.
	 */
	private ControlPanelGenerator(ControlPanelAddon addon, User user, ControlPanelObject controlPanel, String topLabel)
	{
		this.addon = addon;
		this.user = user;
		this.controlPanel = controlPanel;

		this.topLabel = topLabel;
	}


	/**
	 * This method opens Panel for user with given panel settings.
	 * @param addon ControlPanelAddon instance.
	 * @param user User who opens panel.
	 * @param controlPanel ControlPanelSettings that must be constructed
	 * @param topLabel Main command.
	 */
	public static void open(ControlPanelAddon addon, User user, ControlPanelObject controlPanel, String topLabel)
	{
		new ControlPanelGenerator(addon, user, controlPanel, topLabel).build();
	}


// ---------------------------------------------------------------------
// Section: Methods
// ---------------------------------------------------------------------


	/**
	 * This method build panel.
	 */
	private void build()
	{
		PanelBuilder panelBuilder = new PanelBuilder().
			user(this.user).
			name(this.controlPanel.getPanelName());

		// Sort and add.
		this.controlPanel.getPanelButtons().stream().
			sorted(Comparator.comparing(ControlPanelButton::getSlot)).
			forEach(button -> {
				if (button.getSlot() < 0 || button.getSlot() > 53 || panelBuilder.slotOccupied(button.getSlot()))
				{
					// TODO: need to manage empty slots as I can put 53 as first item and go *** other buttons :)
					panelBuilder.item(this.generateButton(button));
				}
				else
				{
					panelBuilder.item(button.getSlot(), this.generateButton(button));
				}
			});

		panelBuilder.build();
	}


	/**
	 * This method generates PanelItem from elements that is defined in ControlPanelButton object.
	 * @param button ControlPanelButton which must be transformed to PanelItem.
	 * @return PanelItem that corresponds to ControlPanelButton
	 */
	private PanelItem generateButton(ControlPanelButton button)
	{
		final boolean runAsServer = button.getCommand().startsWith("[server]");
		final String parsedCommand = button.getCommand().
			replace("[label]", this.topLabel).
			replace("[server]", "").
			replace("[player]", this.user.getName()).
			trim();

		String description = this.addon.getPlugin().getPlaceholdersManager().
			replacePlaceholders(this.user.getPlayer(), button.getDescription());

		return new PanelItemBuilder().
			name(parsedCommand.isEmpty() ? " " : "/" + parsedCommand).
			icon(button.getMaterial() == null ? Material.PAPER : button.getMaterial()).
			description(GuiUtils.stringSplit(description, 999)).
			clickHandler((panel, user, clickType, slot) -> {

				if (!parsedCommand.isEmpty())
				{
					if (runAsServer)
					{
						if (!this.addon.getServer().dispatchCommand(
							this.addon.getServer().getConsoleSender(),
							parsedCommand))
						{
							this.addon.logError("Problem executing command executed by server!");
							this.addon.logError("Command was : " + parsedCommand);
						}
					}
					else
					{
						if (!user.performCommand(parsedCommand))
						{
							this.addon.logError("Problem executing command executed by player!");
							this.addon.logError("Command was : " + parsedCommand);
						}
					}
				}

				return true;
			}).
			build();
	}


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------

	/**
	 * Instance of Addon
	 */
	private ControlPanelAddon addon;

	/**
	 * User who wants to open panel.
	 */
	private User user;

	/**
	 * Object that holds information about custom panel structure.
	 */
	private ControlPanelObject controlPanel;

	/**
	 * Main command label string
	 */
	private String topLabel;
}
