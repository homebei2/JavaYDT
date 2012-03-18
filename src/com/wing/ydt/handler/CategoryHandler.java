package com.wing.ydt.handler;
import com.wing.ydt.vo.Category;
public class CategoryHandler extends NormalHander{
	private Category category ;
	public CategoryHandler(){
		super();
	}
	@Override
	public void endParserElement(String uri, String localName, String qName) {
		// TODO Auto-generated method stub
		switch(flag){
		case 1:
			break;
		case 2:
			category.setCategory_type(Integer.parseInt(sbf.toString().trim()));
			break;
		case 3:
			category.setCategory_name(sbf.toString().trim());
			break;
		case 4:
			category.setCategory_desc(sbf.toString().trim());
			categoryList.add(category);
			break;
		}
	}
	@Override
	public void startParserElement(String uri, String localName, String qName) {
		// TODO Auto-generated method stub
		if("category".equals(localName)){
			category = new Category();
			flag=1;
		}else if("type".equals(localName))
			flag=2;
		else if("name".equals(localName))
			flag=3;
		else if("desc".equals(localName))
			flag=4;
		else
			flag=0;
	}
	
}