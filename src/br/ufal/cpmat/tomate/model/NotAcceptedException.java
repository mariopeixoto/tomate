package br.ufal.cpmat.tomate.model;

public class NotAcceptedException implements Result {

	private String tapeContent;
	
	public NotAcceptedException(String tapeContent) {
		// TODO Auto-generated constructor stub
		this.tapeContent = tapeContent;
	}
	
	public String getMessage() {
		// TODO Auto-generated method stub
		return "Máquina rejeitada com conteúdo da fita: "+ tapeContent;
	}

}
