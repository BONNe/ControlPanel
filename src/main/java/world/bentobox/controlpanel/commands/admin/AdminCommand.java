package world.bentobox.controlpanel.commands.admin;


import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.controlpanel.ControlPanelAddon;
import world.bentobox.controlpanel.utils.Constants;


/**
 * This class process /{gamemode_admin_command} example command call.
 */
public class AdminCommand extends CompositeCommand
{
	/**
	 * This is simple constructor for initializing /{gamemode_admin_command} example command.
	 * @param addon Our Example addon.
	 * @param parentCommand Parent Command where we hook our command into.
	 */
	public AdminCommand(ControlPanelAddon addon, CompositeCommand parentCommand)
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
		this.setPermission("controlpanel.admin");
		this.setParametersHelp(Constants.COMMANDS + "admin.help.parameters");
		this.setDescription(Constants.COMMANDS + "admin.help.description");

		// Import Command ?
		new ImportCommand(this.getAddon(), this);
		// Edit Panel ?

		// Settings Panel ?
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
		this.showHelp(this, user);
		return true;
	}
}
