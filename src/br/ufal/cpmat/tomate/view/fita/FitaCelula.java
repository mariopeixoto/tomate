package br.ufal.cpmat.tomate.view.fita;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class FitaCelula extends JPanel {

	private JLabel symbol;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3874421654131941439L;

	public FitaCelula(Character symbol) {
		super();
		
		this.symbol = new JLabel(symbol.toString());
		
		this.setBackground(Color.WHITE);
		this.setBorder(new Border() {

			@SuppressWarnings("deprecation")
			public Insets getBorderInsets(Component c) {
				// TODO Auto-generated method stub
				return insets();
			}

			public boolean isBorderOpaque() {
				// TODO Auto-generated method stub
				return true;
			}

			public void paintBorder(Component c, Graphics g, int x, int y,
					int width, int height) {
				// TODO Auto-generated method stub
				g.drawRect(x, y, width-1, height-1);
			}
			
		});
		
		this.add(this.symbol);
	}
	
	public void select() {
		this.setBackground(Color.DARK_GRAY);
		symbol.setForeground(Color.WHITE);
	}
}
