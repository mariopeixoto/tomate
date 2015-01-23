package br.ufal.cpmat.tomate.control;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import br.ufal.cpmat.tomate.view.TomateView;

public class ExecutarAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 535305378027791707L;
	
	private TomateView view;
	
	public ExecutarAction(TomateView view) {
		// TODO Auto-generated constructor stub
		this.view = view;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		view.executarAction();
	}

}
