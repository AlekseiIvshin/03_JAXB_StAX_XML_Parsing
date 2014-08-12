package jaxbclasses;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subcategoryType", propOrder = {
    "products"
})
public class SubcategoryType {

	public SubcategoryType.Products products;
	
	@XmlAttribute(required = true)
	public String name;
	
	
	public SubcategoryType(){}
	
	public SubcategoryType(String name){
		products = new Products();
		this.name = name;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "product"
    })
	public static class Products{
		protected List<ProductType> product;
		
		public Products() {}
		
		public List<ProductType> getProduct(){
			if(product == null) {
				product = new ArrayList<ProductType>();
			}
			return product;
		}
	}
}
