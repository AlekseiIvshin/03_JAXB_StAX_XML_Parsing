package jaxbclasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import main.Parser;

/**
 * JAXB parser
 * 
 * @author Aleksei_Ivshin
 *
 */
public class JAXBParser implements Parser {

	public void parse(Schema schema, String xmlLocation, String resultPath) {
		File f = new File(xmlLocation);
		try {
			JAXBContext context = JAXBContext.newInstance(Categories.class);
			Unmarshaller unmarsheller = context.createUnmarshaller();
			Marshaller m = context.createMarshaller();
			unmarsheller.setSchema(schema);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// Get categories from XML
			Categories category = (Categories) unmarsheller
					.unmarshal(new FileInputStream(f));
			// Create new object for adding products
			Categories newCategories = new Categories();
			int deletedCount = 0;
			for (CategoryType ct : category.getCategory()) {
				CategoryType newCT = new CategoryType(ct.name);
				for (SubcategoryType st : ct.subcategories.getSubcategory()) {
					SubcategoryType newST = new SubcategoryType(st.name);
					for (ProductType pt : st.products.getProduct()) {
						if (pt.count > 0) {
							newST.products.getProduct().add(pt);
						} else {
							deletedCount++;
							System.out
									.printf("Deleted: %s/%s/%s %s%n", ct.name,
											st.name, pt.manufacturer, pt.model);
						}
					}
					if (newST.products.getProduct().size() > 0) {
						newCT.subcategories.getSubcategory().add(newST);
					}
				}
				if (newCT.subcategories.getSubcategory().size() > 0) {
					newCategories.getCategory().add(newCT);
				}
			}
			System.out.printf("Deleted %d items%n", deletedCount);
			m.setSchema(schema);
			m.marshal(newCategories, new FileOutputStream(resultPath));
		} catch (Exception e) {
			System.err.println("Error in unmarshalling file: " + f.getName());
			e.printStackTrace();
		}
	}

	@Override
	public boolean isValid(Schema schema, String xmlLocation) {
		Validator validator = schema.newValidator();
		Source source = new StreamSource(xmlLocation);
		try {
			validator.validate(source);
			return true;
		} catch (SAXException | IOException e) {
			return false;
		}
	}

}
