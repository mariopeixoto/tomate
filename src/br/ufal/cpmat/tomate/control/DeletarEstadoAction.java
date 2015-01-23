package br.ufal.cpmat.tomate.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import br.ufal.cpmat.tomate.view.TomateView;

public class DeletarEstadoAction implements KeyListener {

	private TomateView view;
	
	public DeletarEstadoAction(TomateView view) {
		// TODO Auto-generated constructor stub
		this.view = view;
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if ( e.getKeyCode() == KeyEvent.VK_DELETE )
			view.deletarCelulaAction();
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
