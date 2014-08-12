package jaxbclasses;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "productType", propOrder = {"manufacturer","model","date","color","price","count"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductType {
	@XmlElement(required = true)
	public String manufacturer;
	
	@XmlElement(required = true)
	public String model;
	
	// Need in replace String to Date and adding geter and setter(with parse date)
	@XmlElement(required = true)
	public String date;
	
	@XmlElement(required = true, defaultValue = "none")
	public String color;
	
	@XmlElement(required = true)
	public BigDecimal price;
	
	@XmlElement(required = true, defaultValue = "0")
	@XmlSchemaType(name = "nonNegativeInteger")
	public int count;
	
	public ProductType(){}
	
	public ProductType(String manufacturer, String model, String date,
			String color,BigDecimal price,int count){
		this.manufacturer = manufacturer;
		this.model = model;
		this.date = date;
		this.color = color;
		this.price = price;
		this.count = count;
	}
}
