package org.colormine.servlet.validation;

import java.util.ArrayList;

public interface IValidationResult {
	boolean isValid();

	ArrayList<String> getErrorMessage();
}