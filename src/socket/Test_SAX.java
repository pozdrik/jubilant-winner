package socket;

import jdk.internal.org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.jar.Attributes;

public class Test_SAX {
    /*public static void main(String[] args) throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(new File("a.xml"), new DefaultHandler() {

            @Override
            public void startElement (String uri, String localName, String qName, Attributes attributes){
                System.out.println(localName);
                if (qName == "myTagName")
                    System.out.println("tag found");
            }

            // setValidating(booleanvalidating) ddt валидация
            // используем xmlschema -  валидацию выкл
            @Override
            public void startDocument () {
                System.out.println("Document started");
            }
        });
    }*/
}
