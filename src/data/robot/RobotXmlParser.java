package data.robot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class RobotXmlParser extends DefaultHandler {
	private final List<Robot> list = Collections.synchronizedList(new ArrayList<Robot>());
	
	private boolean now_document = false;
	private boolean now_robolist = false;
	private boolean now_robot = false;
	private boolean now_name = false;
	private boolean now_creater = false;
	private boolean now_grade = false;
	private boolean now_img = false;
	private boolean now_desc = false;

	private int now_robot_id = -1;
	private String now_robot_name = "";
	private String now_robot_creater = "";
	private String now_robot_grade = "";
	private String now_robot_img = "";
	private String now_robot_desc = "";

	public RobotXmlParser(String data) {
		xml_process( data );
	}

	private void xml_process(String data) {
        SAXParserFactory spfactory = SAXParserFactory.newInstance();
        SAXParser parser;
		try {
			parser = spfactory.newSAXParser();
	        parser.parse(new ByteArrayInputStream(data.getBytes("utf-8")), this);		
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void startDocument() {
		now_document = true;
	}
	public void endDocument() {
		now_document = false;
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
		if( qName.equals("roboList") ){
			now_robolist = true;
		}else if( qName.equals("robot") ){
			now_robot = true;
			now_robot_id = Integer.parseInt( atts.getValue(0) );
		}else if( qName.equals("name") ){
			now_name = true;
		}else if( qName.equals("creater") ){
			now_creater = true;
		}else if( qName.equals("grade") ){
			now_grade = true;
		}else if( qName.equals("img") ){
			now_img = true;
		}else if( qName.equals("desc") ){
			now_desc = true;
		}else{
			System.out.println("start tag error : " + qName);
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) {
		if( qName.equals("roboList") ){
			now_robolist = false;
		}else if( qName.equals("robot") ){
			now_robot = false;
			String data = "" + now_robot_name + "," + now_robot_creater + "," + now_robot_grade + "," + now_robot_img + "," + now_robot_desc;
			list.add(new Robot(now_robot_id, data));
		}else if( qName.equals("name") ){
			now_name = false;
		}else if( qName.equals("creater") ){
			now_creater = false;
		}else if( qName.equals("grade") ){
			now_grade = false;
		}else if( qName.equals("img") ){
			now_img = false;
		}else if( qName.equals("desc") ){
			now_desc = false;
		}else{
			System.out.println("end tag error : " + qName);
		}
	}
	
	public void characters(char[] ch, int offset, int length) {
		if( now_document == true && now_robolist == true && now_robot == true ){
			if( now_name == true ){
				now_robot_name = String.valueOf( ch, offset, length );
			}else if( now_creater == true ){
				now_robot_creater = String.valueOf( ch, offset, length );
			}else if( now_grade == true ){
				now_robot_grade = String.valueOf( ch, offset, length );
			}else if( now_img == true ){
				now_robot_img = String.valueOf( ch, offset, length );
			}else if( now_desc == true ){
				now_robot_desc = String.valueOf( ch, offset, length );
			}
		}
	}
	
	public List<Robot> get_list(){
		return list;
	}

}
