package main;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.Scanner;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import staxclasses.StAXParser;
import jaxbclasses.JAXBParser;

/** Main class
 * 
 * @author Aleksei_Ivshin
 *
 */
public class XmlParser {

	/** Default mode value
	 */
	private final static String MODE = "jaxb";
	/** Path to XML file
	 */
	public static String xmlLocation;
	
	/** Path to schema file
	 */
	public static String schemaLocation;
	
	/** Mode for XML parsing
	 * [ jaxb, stax ]
	 */
	public static String mode;
	
	public static void main(String[] args) {
		getParams(args);
		showMenu();
	}
	
	/**
	 * Show menu and handle input keys
	 */
	private static void showMenu(){
		System.out.println("JAXB and StAX parser.");
		System.out.println("1 - use JAXB;");
		System.out.println("2 - use StAX;");
		System.out.println("0 - exit.");
		
		Scanner sc = new Scanner(new BufferedInputStream(System.in));
		String line;
		boolean closed = false;
		while (!closed && (line = sc.next())!=null ) {
			switch (line) {
			case "0":
				closed = true;
				continue;
			case "1":
				mode = "jaxb";
				break;
			case "2":
				mode = "stax";
				break;
			default:
				System.out.println("Wrong key");
				continue;
			}
			parse();
			System.out.println("1 - use JAXB;");
			System.out.println("2 - use StAX;");
			System.out.println("0 - exit.");
		}
		sc.close();
		System.out.println("Goodbye!");
	}
	
	/**
	 * Parse XML document.
	 */
	private static void parse(){
		File f = new File(xmlLocation);
		if( !f.exists() || !f.isFile() || !f.canRead()){
			System.err.println("Xml not founded or can't read!");
			return;
		}
		// Init schema
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaLoc = new File(schemaLocation);
		Schema schema;
		try {
			schema = factory.newSchema(schemaLoc);
		} catch (SAXException e1) {
			System.err.println("Error in schema init");
			return;
		}
		// Parser mode
		String resultPath;
		Parser parser;
		switch (mode) {
		case "jaxb":
			resultPath = System.getProperty("user.dir")+"\\resources\\resultJAXB.xml";
			parser = new JAXBParser();
			break;
		case "stax":
			resultPath = System.getProperty("user.dir")+"\\resources\\resultStAX.xml";
			parser = new StAXParser();
			break;
		default:
			resultPath = System.getProperty("user.dir")+"\\resources\\resultJAXB.xml";
			parser = new JAXBParser();
			break;
		}
		boolean preValid = parser.isValid(schema, xmlLocation);
		System.out.println("Sourse xml valid = "+preValid);
		if(!preValid)
			return;
		parser.parse(schema, xmlLocation, resultPath);
		System.out.println("Result xml valid = "+parser.isValid(schema, resultPath));
	}
	
	/**
	 * Read params from args.
	 * -sl [schema location]
	 * -xl [XML file location]
	 * @param args Arguments from command line
	 */
	private static void getParams(String[] args){
		for(int i = 0; i < args.length-1; i+=2){
			switch (args[i]) {
			case "-sl":
				schemaLocation = args[i+1];
				break;
			case "-xl":
				xmlLocation = args[i+1];
				break;
			default:
				break;
			}
		}
		setDefaultParams();
	}

	/**
	 * Set default location of schema and XML files.
	 * Current directory +\resources\... 
	 */
	private static void setDefaultParams(){
		if(xmlLocation == null || xmlLocation.length()==0){
			xmlLocation = System.getProperty("user.dir")+"\\resources\\products.xml";
			System.out.println("Set default path for xml: "+xmlLocation);
		}
		if(schemaLocation == null || schemaLocation.length()==0){
			schemaLocation = System.getProperty("user.dir")+"\\resources\\products.xsd";
			System.out.println("Set default path for schema: "+schemaLocation);
		}
		if(mode == null || mode.length()==0){
			mode = MODE;
		}
		
	}

}
