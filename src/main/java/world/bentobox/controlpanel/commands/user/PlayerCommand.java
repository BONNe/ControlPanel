package world.bentobox.controlpanel.commands.user;


import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.controlpanel.ControlPanelAddon;
import world.bentobox.controlpanel.database.objects.ControlPanelObject;
import world.bentobox.controlpanel.panels.ControlPanelGenerator;
import world.bentobox.controlpanel.utils.Constants;


/**
 * This class process /{gamemode_player_command} example command call.
 */
public class PlayerCommand extends CompositeCommand
{
	/**
	 * This is simple constructor for initializing /{gamemode_player_command} example command.
	 * @param addon Our Example addon.
	 * @param parentCommand Parent Command where we hook our command into.
	 */
	public PlayerCommand(ControlPanelAddon addon, CompositeCommand parentCommand)
	{
		super(addon, parentCommand, "controlpanel", "cp");
	}


	/**
	 * Setups anything that is needed for this command. <br/><br/> It is recommended you
	 * do the following in this method:
	 * <ul>
	 * <li>Register any of the sub-commands of this command;</li>
	 * <li>Define the permission required to use this command using {@link
	 * CompositeCommand#setPermission(String)};</li>
	 * <li>Define whether this command can only be run by players or not using {@link
	 * CompositeCommand#setOnlyPlayer(boolean)};</li>
	 * </ul>
	 */
	@Override
	public void setup()
	{
		this.setPermission("controlpanel");
		this.setOnlyPlayer(true);
		this.setParametersHelp(Constants.COMMANDS + "help.parameters");
		this.setDescription(Constants.COMMANDS + "help.description");
	}


	/**
	 * Returns whether the command can be executed by this user or not. It is recommended
	 * to send messages to let this user know why they could not execute the command. Note
	 * that this is run previous to {@link #execute(User, String, List)}.
	 *
	 * @param user the {@link User} who is executing this command.
	 * @param label the label which has been used to execute this command. It can be
	 * {@link CompositeCommand#getLabel()} or an alias.
	 * @param args the command arguments.
	 * @return {@code true} if this command can be executed, {@code false} otherwise.
	 * @since 1.3.0
	 */
	@Override
	public boolean canExecute(User user, String label, List<String> args)
	{
		this.panelObject = ((ControlPanelAddon) this.getAddon()).
			getAddonManager().getUserControlPanel(user, this.getWorld(), this.getPermissionPrefix());

		if (this.panelObject == null)
		{
			if (user.isOp())
			{
				user.sendMessage(Constants.ERRORS + "no-valid-panels-op");
			}
			else
			{
				user.sendMessage(Constants.ERRORS + "no-valid-panels");
			}

			return false;
		}
		else
		{
			return true;
		}
	}


	/**
	 * Defines what will be executed when this command is run.
	 *
	 * @param user the {@link User} who is executing this command.
	 * @param label the label which has been used to execute this command. It can be
	 * {@link CompositeCommand#getLabel()} or an alias.
	 * @param args the command arguments.
	 * @return {@code true} if the command executed successfully, {@code false} otherwise.
	 */
	@Override
	public boolean execute(User user, String label, List<String> args)
	{
		// Execute panel
		ControlPanelGenerator.open((ControlPanelAddon) this.getAddon(),
			user,
			this.panelObject,
			this.getTopLabel());

		return true;
	}


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------

	/**
	 * Panel that must be opened.
	 */
	private ControlPanelObject panelObject;
}
