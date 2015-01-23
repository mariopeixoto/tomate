package br.ufal.cpmat.tomate.control;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import br.ufal.cpmat.tomate.view.TomateView;

public class PararSimulacaoAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1182450742215644340L;
	
	private TomateView view;
	
	public PararSimulacaoAction(TomateView view) {
		// TODO Auto-generated constructor stub
		this.view = view;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		view.pararSimulacaoAction();
	}

}
