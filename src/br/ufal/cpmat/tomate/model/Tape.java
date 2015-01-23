package br.ufal.cpmat.tomate.model;

import java.util.ArrayList;
import java.util.List;

public class Tape {
	
	private List<Character> tape;
	private Character blankChar;
	private Character initChar;
	private int marker;
	
	public Tape(Character blankChar,Character initChar) {
		this.tape = new ArrayList<Character>();
		this.blankChar = blankChar;
		this.initChar = initChar;
		this.marker = 0;
	}
	
	public void write ( String tape ) {
		this.tape.clear();
		this.marker = 0;
		this.tape.add(initChar);
		for ( int i=0;i<tape.length();i++ ) {
			this.tape.add ( tape.charAt ( i ) );
		}
	}
	
	public void clear ( ) {
		this.tape.clear();
	}
	
	public int getMarker() {
		return this.marker;
	}
	
	public void write ( Character c ) {
		this.tape.set ( marker, c );
	}

	public Character read ( ) {
		return this.tape.get(marker);
	}
	
	public void moveRight() {
		if ( this.marker == tape.size()-1 ) {
			this.tape.add(blankChar);
		}		
		this.marker++;
	}
	
	public void moveLeft() throws CantMoveLeftException {
		if ( this.marker == 0 ) {
			throw new CantMoveLeftException("Começo da fita, impossível mover à esquerda.");
		}
		else if ( this.marker == tape.size()-1 && this.tape.get(marker).equals(blankChar) ) {
			this.tape.remove(marker);
		}
		this.marker--;		
	}
	
	public String toString() {
		String out = "";
		for( Character c : tape ) {
			out += c;
		}
		return out;
	}
}

