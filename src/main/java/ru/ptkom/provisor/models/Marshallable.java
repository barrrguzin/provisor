package ru.ptkom.provisor.models;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

@Slf4j
public class Marshallable {

    public String toXML() {

        Object objectToMarshall = this;
        Class objectClass = objectToMarshall.getClass();
        StringWriter outputXmlStringWriter = new StringWriter();

        try {
            JAXBContext context = JAXBContext.newInstance(objectClass);
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(objectToMarshall, outputXmlStringWriter);
            String xml = outputXmlStringWriter.toString();
            return xml;

        } catch (JAXBException e) {

            log.error("Marshalling end with error: " + e.getMessage());
            throw new RuntimeException("Marshalling end with error: " + e.getMessage());

        }
    }
}
