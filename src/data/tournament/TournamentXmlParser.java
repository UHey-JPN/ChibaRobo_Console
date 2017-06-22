package data.tournament;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class TournamentXmlParser extends DefaultHandler {
	private GameNode root;
	Deque<GameNode> stack = new ArrayDeque<GameNode>();

	
	private boolean now_document = false;
	private boolean now_tournament = false;

	
	public TournamentXmlParser(String data) {
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
		if( qName.equals("tournament") ){
			now_tournament = true;
		}else{
			if( now_tournament && now_document ){
				if( qName.equals("game") ){
					GameNode now_game = new GameNode();
					now_game.set_number(Integer.parseInt(atts.getValue(0)));
					try{
						GameNode g = stack.element();
						if(g.get_game(0) == null){
							g.set_game(0, now_game);
						}else{
							g.set_game(1, now_game);
						}
					}catch(NoSuchElementException e){
						// root instance
						root = now_game;
					}finally{
						stack.push(now_game);
					}
				
				}else if( qName.equals("team") ){
					GameNode now_game = new GameNode(Integer.parseInt(atts.getValue(0)));
					try{
						GameNode g = stack.element();
						if(g.get_game(0) == null){
							g.set_game(0, now_game);
						}else{
							g.set_game(1, now_game);
						}
					}catch(NoSuchElementException e){
						root = now_game;
					}finally{
						stack.push(now_game);
					}
					
				}else{
					System.out.println("start tag error : " + qName);
				}
			}
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName) {
		if( qName.equals("tournament") ){
			now_tournament = false;
		}else if( qName.equals("game") || qName.equals("team") ){
			stack.remove();
		}else{
			System.out.println("end tag error : " + qName);
		}
	}
	
	public void characters(char[] ch, int offset, int length) {
		;
	}
	
	public GameNode get_root(){
		return root;
	}

}
