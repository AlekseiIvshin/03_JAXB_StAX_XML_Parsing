package jaxbclasses;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder = "category",namespace="http://www.w3.org/2001/XMLSchema")
public class Categories {

	private List<CategoryType> category;
	
	public  Categories () {}
	
	public void setCategory(List<CategoryType> category){
		this.category = category;
	}
	
	public List<CategoryType> getCategory(){
		if(category == null){
			category = new ArrayList<CategoryType>();
		}
		return category;
	}
}
