package com.cooksys.quiz_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {

    /*
    How to create a generated version UID in Intellij

    1.Declare the variable like below.

        private static final long serialVersionUID;

    2. Highlight the line and Press Alt + Enter

    3. Choose the "Randomly Change 'serialVersionUID' initializer"

     */

    private static final long serialVersionUID = 5067213732704459927L;

    private String message;

}
