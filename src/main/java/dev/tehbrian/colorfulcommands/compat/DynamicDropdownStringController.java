package dev.tehbrian.colorfulcommands.compat;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.gui.controllers.dropdown.DropdownStringController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DynamicDropdownStringController extends DropdownStringController {
    private final Supplier<List<String>> allowedValues;

    public DynamicDropdownStringController(final Option<String> option, final Supplier<List<String>> allowedValues, final boolean allowEmptyValue, final boolean allowAnyValue) {
        super(option, List.of(), allowEmptyValue, allowAnyValue);
        this.allowedValues = allowedValues;
    }

    @Override
    public List<String> getAllowedValues(String inputField) {
        List<String> values = new ArrayList<>(allowedValues.get());
        if (allowEmptyValue && !values.contains("")) values.add("");
        if (allowAnyValue && !inputField.isBlank() && !values.contains(inputField)) values.add(inputField);
        String currentValue = getString();
        if (allowAnyValue && !values.contains(currentValue)) values.add(currentValue);
        return values;
    }

}
