package seedu.address.logic.commands.general;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Shows the overall statistics.
 */
public class StatsCommand extends Command {
    public static final String COMMAND_WORD = "stats";

    public static final String MESSAGE_SUCCESS = "Overall statistics for pets, recent schedule, and list of food.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPetList(Model.PREDICATE_SHOW_ALL_PETS);
        model.updateFilteredSlotList(Model.PREDICATE_SHOW_ALL_SLOTS);
        // model.up(Model.PREDICATE_SHOW_ALL_FOOD_COLLECTIONS);
        return new CommandResult(MESSAGE_SUCCESS, false, false, false, true);
    }

}