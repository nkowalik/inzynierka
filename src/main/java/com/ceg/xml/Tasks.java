package com.ceg.xml;

import javax.xml.bind.annotation.*;

/**
 *
 * @author Natalia
 */

@XmlRootElement
public class Tasks {
    private TaskData taskData;

    @XmlElement(name="task-data")
    public TaskData getTaskData() {
        return taskData;
    }

    public void setTaskdata(TaskData taskData) {
        this.taskData = taskData;
    }
}
