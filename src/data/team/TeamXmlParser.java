package data.team;

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


public class TeamXmlParser extends DefaultHandler {
	private final List<Team> list = Collections.synchronizedList(new ArrayList<Team>());
	
	private boolean now_document = false;
	private boolean now_teamlist = false;
	private boolean now_team = false;
	private boolean now_name = false;
	private boolean now_desc = false;
	
	private int now_team_id = -1;
	private int robo_cnt = 0;
	private int[] now_robot_id = new int[2];
	private String now_team_name = "";
	private String now_team_desc = "";

	
	public TeamXmlParser(String data) {
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
		if( qName.equals("teamList") ){
			now_teamlist = true;
		}else if( qName.equals("team") ){
			now_team_id = Integer.parseInt( atts.getValue(0) );
			now_team = true;
		}else if( qName.equals("name") ){
			now_name = true;
		}else if( qName.equals("robot") ){
			now_robot_id[robo_cnt] = Integer.parseInt( atts.getValue(0) );
			robo_cnt++;
		}else if( qName.equals("desc") ){
			now_desc = true;
		}else{
			System.out.println("start tag error : " + qName);
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) {
		if( qName.equals("teamList") ){
			now_teamlist = false;
		}else if( qName.equals("team") ){
			now_team = false;
			robo_cnt = 0;
			
			list.add(new Team(now_team_id, now_team_name, now_robot_id[0], now_robot_id[1], now_team_desc));

		}else if( qName.equals("name") ){
			now_name = false;
		}else if( qName.equals("robot") ){
			;
		}else if( qName.equals("desc") ){
			now_desc = false;
		}else{
			System.out.println("end tag error : " + qName);
		}
	}
	
	public void characters(char[] ch, int offset, int length) {
		if( now_document == true && now_teamlist == true && now_team == true ){
			if( now_name == true ){
				now_team_name = String.valueOf( ch, offset, length );
			}else if( now_desc == true ){
				now_team_desc = String.valueOf( ch, offset, length );
			}
		}
	}
	
	public List<Team> get_list(){
		return list;
	}

}
