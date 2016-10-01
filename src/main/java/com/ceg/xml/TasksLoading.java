package com.ceg.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class TasksLoading {

    /**
     * Laduje polecenia ze z góry zdefiniowanego pliku .xml i zapisuje w obiekcie TaskData.
     * @return Obiekt zawierający dane o typach zadań.
     */
    public static TaskData loadFromXml() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Tasks.class);
            File file = new File("tasks.xml");

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            TaskData taskData = (TaskData) unmarshaller.unmarshal(file);

            return taskData;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
