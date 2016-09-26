package com.ceg.examContent;

import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Klasa abstrakcyjna będąca szablonem dla parametrów wszystkich zadań.
 */
@XmlSeeAlso({
        TaskParametersComplexOutput.class,
        TaskParametersGaps.class,
        TaskParametersLineNumbers.class,
        TaskParametersReturnedValue.class,
        TaskParametersSimpleOutput.class,
        TaskParametersVarValue.class
})
abstract public class TaskParameters {
    private int noOfAnswers;

    public int getNoOfAnswers() {
        return noOfAnswers;
    }
    public void setNoOfAnswers(int noOfAnswers) {
        this.noOfAnswers = noOfAnswers;
    }
}
