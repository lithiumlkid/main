package seedu.address.logic.commands;

//@@author lohtianwei
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import com.google.common.hash.Hashing;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.UserPrefs;

/**
 * Sets lock in model to true/false depending on current state
 */
public class KeyCommand extends Command {
    public static final String COMMAND_WORD = "key";
    public static final String COMMAND_ALIAS = "k";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Toggles the lock on MTM. "
            + "Functionality of MTM reduced\n"
            + "Parameters: PASSWORD"
            + "Example: " + COMMAND_WORD
            + " myPassword";

    public static final String MESSAGE_SUCCESS = "MTM lock toggled!";
    public static final String MESSAGE_WRONG_PASS = "Password entered is incorrect. Please try again.";

    private String password;

    private final Logger logger = LogsCenter.getLogger(KeyCommand.class);

    public KeyCommand(String password) {
        this.password = password;
    }

    /**
     * Checks if input password is correct.
     */
    private boolean correctPassword() {
        UserPrefs up = model.getUserPrefs();
        String hash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        return hash.equals(up.getAddressBookHashedPass());
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (correctPassword()) {
            if (model.getLockState()) {
                model.unlockAddressBookModel();
            } else {
                model.lockAddressBookModel();
            }

            logger.info("Lock state is now: " + Boolean.toString(model.getLockState()));
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            throw new CommandException(MESSAGE_WRONG_PASS);
        }
    }
}
