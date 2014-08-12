package main;

import javax.xml.validation.Schema;

public interface Parser {

	/**
	 * Validation xml by schema
	 * @param schema Schema for validation
	 * @param xmlLocation Path to XML file
	 * @return
	 */
	public boolean isValid(Schema schema,String xmlLocation);
	
	/**
	 * Parse XML file and create new file with results
	 * @param schema
	 * @param xmlLocation
	 * @param resultFile
	 */
	public void parse(Schema schema,String xmlLocation,String resultFile);
}
