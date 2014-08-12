package jaxbclasses;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="categoryType",propOrder = "subcategories")
public class CategoryType {
	
	@XmlAttribute(required = true)
	public String name;
	
	public CategoryType.Subcategories subcategories;
	
	public CategoryType(){}
	
	public CategoryType(String name){
		subcategories = new Subcategories();
		this.name = name;
	}
	
	@XmlRootElement
	public static class Subcategories{
		
		private List<SubcategoryType> subcategory;
		
		public Subcategories() {}
		
		public void setSubcategory(List<SubcategoryType> value){
			this.subcategory = value;
		}
		
		public List<SubcategoryType> getSubcategory(){
			if(subcategory == null) {
				subcategory = new ArrayList<SubcategoryType>();
			}
			return subcategory;
		}
		
	}
}
