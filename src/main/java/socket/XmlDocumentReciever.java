package socket;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

public class XmlDocumentReciever {

    XMLEventReader xmlEventReader;
    DocumentBuilder documentBuilder;
    InputStream inputStream;

    public XmlDocumentReciever(InputStream inputStream) throws ParserConfigurationException, XMLStreamException {
        this.inputStream = inputStream;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        documentBuilder = dbf.newDocumentBuilder();


        XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);
        //xmlEventReader.nextEvent();
    }

    public Document receive() throws XMLStreamException, SAXException, IOException{

        XMLEvent xmlEvent;
        Integer countEnd = 0;
        Integer countStart = 0;
        StringBuilder stringBuilder = new StringBuilder();
        //System.out.println(XMLEvent);
        while((xmlEvent = xmlEventReader.nextEvent()) != null) {
            stringBuilder.append(xmlEvent);
            if (xmlEvent.isStartElement()){
                countStart += 1;
            }
            else if (xmlEvent.isEndElement()){
                countEnd += 1;
            }
            if (countEnd.equals(countStart) && !countEnd.equals(0)){
                //Characters stringedDocument = (Characters) xmlEvent;
                return documentBuilder.parse(new ByteArrayInputStream(stringBuilder.toString().getBytes("UTF-8")));
            }
            /*if (xmlEvent.isCharacters()) {
                break;
            }*/
        };
        return null;

    }


    public void close() {
        try{
            inputStream.close();
        } catch (Exception e) {

        }
    }

}