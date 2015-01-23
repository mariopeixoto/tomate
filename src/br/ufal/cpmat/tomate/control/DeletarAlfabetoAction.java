package br.ufal.cpmat.tomate.control;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import br.ufal.cpmat.tomate.view.TomateView;

public class DeletarAlfabetoAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -254441718661430329L;
	
	private TomateView view;
	
	public DeletarAlfabetoAction(TomateView view) {
		// TODO Auto-generated constructor stub
		this.view = view;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		view.deletarAlfabetoAction();
	}

}
