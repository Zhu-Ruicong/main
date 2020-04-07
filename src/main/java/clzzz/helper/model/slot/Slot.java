package clzzz.helper.model.slot;

import static clzzz.helper.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

import clzzz.helper.commons.util.DateTimeUtil;
import clzzz.helper.model.pet.Pet;
import clzzz.helper.ui.DisplaySystemType;
import clzzz.helper.ui.list.DisplayItem;

/**
 * Represents a Slot in the pet store helper schedule system.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Slot implements Comparable<Slot>, DisplayItem {

    private final Pet pet;
    private final LocalDateTime dateTime;
    private final Duration duration;

    /**
     * Every field must be present and not null.
     */
    public Slot(Pet pet, LocalDateTime dateTime, Duration duration) {
        requireAllNonNull(pet, dateTime, duration);
        this.pet = pet;
        this.dateTime = dateTime;
        this.duration = duration;
    }

    public Pet getPet() {
        return pet;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Duration getDuration() {
        return duration;
    }

    /**
     * Returns the starting time of the slot, regardless of its date.
     */
    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    /**
     * Returns the date on which the slot falls on, regardless of time of day.
     */
    public LocalDate getDate() {
        return getDateTime().toLocalDate();
    }

    /**
     * Returns the ending datetime of the slot, based on its starting datetime and duration.
     */

    public LocalDateTime getEndDateTime() {
        return getDateTime().plus(duration);
    }

    /**
     * Returns the ending time of the slot, based on its starting time and duration.
     */
    public LocalTime getEndTime() {
        return getTime().plus(duration);
    }


    /**
     * Returns true if the string is in correct datetime format (might be invalid datetime)
     */
    public static boolean isValidDateTimeFormat(String test) {
        try {
            LocalDateTime mightBeValid = LocalDateTime.parse(test, DateTimeUtil.DATETIME_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true if the string is in correct date format (might be invalid date)
     */
    public static boolean isValidDateFormat(String test) {
        try {
            LocalDate mightBeValid = LocalDate.parse(test, DateTimeUtil.DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * This method should be called after making sure the format is correct.
     * It checks if the date entered is valid.
     */
    public static boolean isValidDate(String test) {
        try {
            String[] tests = test.split("\\s+");
            LocalDate date = LocalDate.parse(tests[0], DateTimeUtil.STRICT_DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * This method should be called after making sure the format is correct.
     * It checks if the time entered is valid.
     */
    public static boolean isValidTime(String test) {
        try {
            String[] tests = test.split("\\s+");
            LocalTime time = LocalTime.parse(tests[1], DateTimeUtil.STRICT_TIME_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true if both slots fall on the same date, regardless of time of day.
     */
    public boolean isSameDate(Slot othersSlot) {
        return getDate().equals(othersSlot.getDate());
    }

    /**
     * Returns true if this slot starts and ends on the same date.
     */
    public boolean isWithinOneDay() {
        LocalDate endDate = getEndDateTime().toLocalDate();
        return getDate().equals(endDate);
    }

    /**
     * Returns true if this slot is in conflict with the given other slot.
     */
    public boolean isInConflictWith(Slot otherSlot) {
        if (this == otherSlot) {
            // otherSlot is a reference to this object,
            // and a slot cannot be in conflict with itself
            return false;
        }
        if (this.equals(otherSlot)) {
            // two separate slots occuring on the same datetime
            // and having the same duration must be in conflict with each other.
            return true;
        }
        LocalDateTime start = getDateTime();
        LocalDateTime end = getEndDateTime();
        LocalDateTime otherStart = otherSlot.getDateTime();
        LocalDateTime otherEnd = otherSlot.getEndDateTime();
        return start.isBefore(otherEnd) && otherStart.isBefore(end);
    }

    /**
     * Returns true if this slot is in conflict with one or more other slots.
     */
    public boolean hasConflict(List<Slot> allSlots) {
        return allSlots.stream()
                .anyMatch(otherSlot -> isInConflictWith(otherSlot));
    }

    /**
     * Compares this slot to another slot.
     *
     * The comparison is first done by their datetimes, then by their durations,
     * then by the names of the pets occupying the slots.
     */
    @Override
    public int compareTo(Slot other) {
        if (dateTime.compareTo(other.dateTime) != 0) {
            return dateTime.compareTo(other.dateTime);
        }
        if (duration.compareTo(other.duration) != 0) {
            return duration.compareTo(other.duration);
        }
        return pet.getName().compareTo(other.pet.getName());
    }

    @Override
    public DisplaySystemType getDisplaySystemType() {
        return DisplaySystemType.SCHEDULE;
    }

    /**
     * Returns true if all fields of both slots are the same.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Slot)) {
            return false;
        }

        Slot otherSlot = (Slot) other;
        return otherSlot.getPet().equals(getPet())
                && otherSlot.getDateTime().equals(getDateTime())
                && otherSlot.getDuration().equals(getDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pet, dateTime, duration);
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s (%s)",
                getDate().format(DateTimeUtil.DATE_FORMAT),
                getTime(), getEndTime(), getPet().getName());
    }
}