package br.ufal.cpmat.tomate.model;

public class InvalidSymbolException implements Result {
	
	private Character symbol;
	
	public InvalidSymbolException(Character symbol) {
		// TODO Auto-generated constructor stub
		this.symbol = symbol;
	}

	public String getMessage() {
		return "Símbolo inválido: '" + symbol +"'";
	}

}
