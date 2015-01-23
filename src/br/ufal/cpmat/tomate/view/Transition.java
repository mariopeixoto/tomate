package br.ufal.cpmat.tomate.view;

import java.io.Serializable;

import br.ufal.cpmat.tomate.model.MovimentType;

public class Transition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8375287639636869270L;
	
	private Character readSymbol,writeSymbol;
	private MovimentType movimentType;
	
	public Transition() {
		
	}
	
	public Transition(Character readSymbol, Character writeSymbol, MovimentType movimentType) {
		this.readSymbol = readSymbol;
		this.writeSymbol = writeSymbol;
		this.movimentType = movimentType;
	}
	
	public Character getReadSymbol() {
		return this.readSymbol;
	}
	
	public Character getWriteSymbol() {
		return this.writeSymbol;
	}
	
	public MovimentType getMovimentType() {
		return movimentType;
	}
	
	public void setReadSymbol(Character symbol) {
		this.readSymbol = symbol;
	}
	
	public void setWriteSymbol(Character symbol) {
		this.writeSymbol = symbol;
	}
	
	public void setMovimentType(MovimentType symbol) {
		this.movimentType = symbol;
	}
	
	public String toString() {
		String type = "";
		if ( movimentType == MovimentType.LEFT )
			type = "E";
		else
			type = "D";
		return "( " + readSymbol + " , " + writeSymbol + " , " + type + " )";
	}
	
}
