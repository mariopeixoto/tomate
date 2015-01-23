package br.ufal.cpmat.tomate.view.fita;

import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JPanel;

public class FitaPanel extends JPanel {

	private Vector<FitaCelula> celulas = new Vector<FitaCelula>();
	/**
	 * 
	 */
	private static final long serialVersionUID = -5304625740740174421L;

	public FitaPanel() {
		// TODO Auto-generated constructor stub
		
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER,0,0);
		this.setLayout(layout);
	}
	
	public void setTape(String tape,int marker) {
		celulas.clear();
		this.removeAll();
		for ( char c : tape.toCharArray() ) {
			FitaCelula cell = new FitaCelula(c);
			this.add(cell);
			celulas.add(cell);		
		}
		celulas.get(marker).select();
		this.updateUI();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FitaPanel().setVisible(true);
                
            }
        });
	}

}
