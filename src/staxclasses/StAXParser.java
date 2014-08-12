package staxclasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import main.Parser;

/** StAX parser
 * 
 * @author Aleksei_Ivshin
 *
 */
public class StAXParser implements Parser{

	public void parse(Schema schema, String xmlLocation, String reslutFile){
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(xmlLocation);
			outputStream = new FileOutputStream(reslutFile,false);
		} catch (FileNotFoundException e) {
			System.err.println("File not found at "+xmlLocation);
				try {
					if(inputStream != null){
						inputStream.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			return;
		}
		
		int deletedCount = 0;
		XMLEventReader reader = null;
		XMLEventWriter writer = null;
		try {
			reader = XMLInputFactory.newInstance().
					createXMLEventReader(inputStream);
			writer = XMLOutputFactory.newInstance().
					createXMLEventWriter(outputStream,"UTF-8");
			
			// Temporary list for product's event(tags) 
			List<XMLEvent> events;
			while(reader.hasNext()) {
				XMLEvent event = (XMLEvent) reader.next();
				// Found start element with name 'product'
				if(event.isStartElement() && 
						event.asStartElement().getName().getLocalPart().equals("product")) {
					String model = "";
					String manufacturer = "";
					// Initialize list events of product
					events = new ArrayList<XMLEvent>();
					// read all tags in product. Do it while tag is not equals </product>
					do{
						events.add(event);
						event = (XMLEvent) reader.nextEvent();
					}while(reader.hasNext() && 
							!(event.isEndElement() && 
							event.asEndElement().getName().getLocalPart().equals("product")));
					// Add some tag
					events.add(event);
					// search tag count and check value
					for(int i = 0; i<events.size(); i++){
						if(events.get(i).isStartElement() && 
								events.get(i).asStartElement().getName().getLocalPart().equals("count")) {
							while(i < events.size() && 
									!(events.get(i).isCharacters() || events.get(i).isEndElement())) {
								i++;
							}
							if(events.get(i).asCharacters().toString().trim().equals("0")) {
								model = getValue(events, "model");
								manufacturer = getValue(events, "manufacturer");
								System.out.printf("Deleted: %s %s%n",manufacturer,model);
								events = new ArrayList<XMLEvent>();
								deletedCount++;
								break;
							}
						}
					}
					// Add tags of product to writer
					for(XMLEvent ev: events) {
						writer.add(ev);
					}
				} else {
					writer.add(event);
				}
			}
			writer.flush();
			
		} catch (XMLStreamException | FactoryConfigurationError e) {
			e.printStackTrace();
		} finally {
			System.out.printf("Deleted %d items%n",deletedCount);
			if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(writer!=null)
				try {
					writer.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			
			if(reader!=null)
				try {
					reader.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
		}
	}

	@Override
	public boolean isValid(Schema schema, String xmlLocation) {

		Validator validator = schema.newValidator();
		Source source = new StreamSource(xmlLocation);
		try{
			validator.validate(source);
			return true;
		} catch (SAXException | IOException e) {
			return false;
		}
	}

	private String getValue(List<XMLEvent> events, String tagName){
		String value="";
		
		for(int i = 0; i<events.size(); i++){
			if(events.get(i).isStartElement() && 
					events.get(i).asStartElement().getName().getLocalPart().equals(tagName)) {
				while(i < events.size() && 
						!(events.get(i).isCharacters() || events.get(i).isEndElement())) {
					i++;
				}
				if(events.get(i).isCharacters()){
					return events.get(i).asCharacters().toString().trim();
				}
			}
		}
		return value;
	}
}
