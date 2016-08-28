package com.ceg.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 *
 * @author Natalia
 */

public class ExamLoading {
    public static ExamData loadFromXml(String fileName) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Tasks.class);
            File file = new File(fileName);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            ExamData examData = (ExamData) unmarshaller.unmarshal(file);

            return examData;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
