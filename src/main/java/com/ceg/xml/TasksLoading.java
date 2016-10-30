package com.ceg.xml;

import com.ceg.utils.Alerts;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class TasksLoading {

    private static final String filename = "tasks.xml";
    /**
     * Laduje polecenia ze z góry zdefiniowanego pliku .xml i zapisuje w obiekcie TaskData.
     * @return Obiekt zawierający dane o typach zadań.
     */
    public static TaskData loadFromXml() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Tasks.class);
            File file = new File(filename);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            TaskData taskData = (TaskData) unmarshaller.unmarshal(file);

            return taskData;
        } catch (JAXBException e) {
            Alerts.wrongFileContentAlert();
        }
        return null;
    }
}
