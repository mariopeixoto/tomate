package br.ufal.cpmat.tomate.control;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import br.ufal.cpmat.tomate.view.TomateView;

public class AbrirAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7897181217301067045L;
	
	private TomateView view;
	
	public AbrirAction(TomateView view) {
		// TODO Auto-generated constructor stub
		this.view = view;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		view.abrirAction();
	}

}
