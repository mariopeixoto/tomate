/*
 * MainWindow.java
 *
 * Created on 21 de Março de 2007, 00:06
 */

package br.ufal.cpmat.tomate.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.event.GraphLayoutCacheListener;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import br.ufal.cpmat.tomate.control.AbrirAction;
import br.ufal.cpmat.tomate.control.AdicionarAlfabetoAction;
import br.ufal.cpmat.tomate.control.DeletarAlfabetoAction;
import br.ufal.cpmat.tomate.control.DeletarEstadoAction;
import br.ufal.cpmat.tomate.control.ExecutarAction;
import br.ufal.cpmat.tomate.control.ExecutarProximoPassoAction;
import br.ufal.cpmat.tomate.control.MudarListaAlfabetoAction;
import br.ufal.cpmat.tomate.control.NovaAction;
import br.ufal.cpmat.tomate.control.PararSimulacaoAction;
import br.ufal.cpmat.tomate.control.SairAction;
import br.ufal.cpmat.tomate.control.SalvarAction;
import br.ufal.cpmat.tomate.control.SimularAction;
import br.ufal.cpmat.tomate.model.MovimentType;
import br.ufal.cpmat.tomate.model.Result;
import br.ufal.cpmat.tomate.model.State;
import br.ufal.cpmat.tomate.model.TomateModelInterface;
import br.ufal.cpmat.tomate.model.Transitions;
import br.ufal.cpmat.tomate.view.cellview.GPCellViewFactory;
import br.ufal.cpmat.tomate.view.fita.FitaPanel;
import br.ufal.cpmat.tomate.view.observers.ResultObserver;

import com.thoughtworks.xstream.XStream;

/**
 *
 * @author  mario
 */
public class MainWindow extends javax.swing.JFrame implements TomateView {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1341043763420462571L;
	
	/** Creates new form MainWindow */
    public MainWindow(String modelClass) {
    	try {
			this.modelClass = Class.forName(modelClass);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        initComponents();
    }
    
    public DefaultGraphCell createVertex(double x,
			double y, double w, double h, Color bg, boolean raised, DefaultGraphCell cell, String viewClass) {
		
		// set the view class (indirection for the renderer and the editor)
		if (viewClass != null)
			GPCellViewFactory.setViewClass(cell.getAttributes(), viewClass);

		// Set bounds
		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(
				x, y, w, h));

		// Set fill color
		if (bg != null) {
			GraphConstants.setGradientColor(cell.getAttributes(), bg);
			GraphConstants.setOpaque(cell.getAttributes(), true);
		}

		// Set raised border
		if (raised)
			GraphConstants.setBorder(cell.getAttributes(), BorderFactory
					.createRaisedBevelBorder());
		else
			// Set black border
			GraphConstants.setBorderColor(cell.getAttributes(), Color.black);

		// Add a Floating Port
		DefaultPort port = new DefaultPort();
		cell.add(port);
		port.setParent(cell);

		return cell;
	}
    
    protected void setSelected(String state) {
    	for( Object obj : cells ) {
    		if ( obj instanceof DefaultEdge ) {
    			
    		}
    		else {
    			DefaultGraphCell cell = (DefaultGraphCell)obj;
    			if ( ((String)cell.getUserObject()).equals(state) ) {
    				graph.setSelectionCell(cell);
    			}
    		}
    	}
    	
    }
    
    private void installListeners() {
    	if ( graphListener != null ){
	    	for( GraphLayoutCacheListener list : graphListener ) {
	    		graph.getGraphLayoutCache().addGraphLayoutCacheListener(list);    		
	    	}
    	}
    	graph.addMouseListener(mouseListener);
        graph.addKeyListener(keyListener);
    }
    
    private void uninstallListeners() {
    	graph.removeMouseListener(mouseListener);
        graph.removeKeyListener(keyListener);
        if ( graphListener != null ){
	        for( GraphLayoutCacheListener list : graphListener ) {
	    		graph.getGraphLayoutCache().removeGraphLayoutCacheListener(list);    		
	    	}
        }
    }
    
    private void newMachine() {
    	((DefaultListModel)alfabeto.getModel()).clear();
    	
    	model = new DefaultGraphModel();
    	view = new GraphLayoutCache(model,new GPCellViewFactory());
    	graph = new JGraph(model,view);
    	
    	graph.setAntiAliased(true);
    	graph.setSizeable(false);
    	
    	cells = new ArrayList<Object>();

		installListeners();
		
        scrollpane.setViewportView(graph);
        
        adicionarAlfabeto.setVisible(true);
        deletarAlfabeto.setVisible(true);
        selectButton.setEnabled(true);
        estadoNormalButton.setEnabled(true);
        estadoFinalButton.setEnabled(true);
        transicaoButton.setEnabled(true);
        alfabetos.setVisible(true);
        scrollpaneAlfabeto.setVisible(true);
        
        index = 1;
        
        alfabetoList.clear();
        alfabetoAuxList.clear();
        this.graphListener = this.graph.getGraphLayoutCache().getGraphLayoutCacheListeners();
        
    }
    
    private void newMachine(TuringMachine machine) {
    	
    	model = machine.getView().getModel();
    	view = machine.getView();
    	graph = new JGraph(model,view);
    	
    	graph.setAntiAliased(true);
    	graph.setSizeable(false);
    	
    	cells = machine.getCells();

		installListeners();
		
        scrollpane.setViewportView(graph);
        
        adicionarAlfabeto.setVisible(true);
        deletarAlfabeto.setVisible(true);
        selectButton.setEnabled(true);
        estadoNormalButton.setEnabled(true);
        estadoFinalButton.setEnabled(true);
        transicaoButton.setEnabled(true);
        alfabetos.setVisible(true);
        scrollpaneAlfabeto.setVisible(true);
        
        index = machine.getIndex();
        
        alfabetoList = machine.getAlfabetoList();
        alfabetoAuxList = machine.getAlfabetoAuxList();
        
        
        DefaultListModel model = ((DefaultListModel)alfabeto.getModel());
        model.clear();
		if ( alfabetos.getSelectedIndex() == 0 ){
			for ( Character symbol : alfabetoList ){
				model.addElement(symbol);
			}
		}
		else{
			for ( Character symbol : alfabetoAuxList ){
				model.addElement(symbol);
			}
		}
        
        
        this.machine = machine.getMachine();
        this.graphListener = this.graph.getGraphLayoutCache().getGraphLayoutCacheListeners();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
    	
    	mouseListener = new MouseListener() {

        	private boolean transicao=false;
        	private DefaultGraphCell i=null,t=null;
        	
			

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
				
				if ( !transicao ){
					String type="";
					if ( estadoNormalButton.isSelected() )
						type="JGraphNormalStateView";
					else if ( estadoFinalButton.isSelected() )
						type="JGraphFinalStateView";
					else if ( transicaoButton.isSelected() ){
						if ( graph.getSelectionCell() instanceof DefaultGraphCell ){
							i = (DefaultGraphCell) graph.getSelectionCell();
							transicao = true;
						}
					}
					if ( type !="" ) {
						DefaultGraphCell newCell = createVertex(e.getX()-25, e.getY()-25, 50, 50, null, false, new DefaultGraphCell("q"+index++), "br.ufal.cpmat.tomate.view.cellview."+type);
						graph.getGraphLayoutCache().insert(newCell);
						cells.add(newCell);
						selectButton.setSelected(true);
					}
				}
				else {
					if( transicaoButton.isSelected() ){
						if ( graph.getSelectionCell() instanceof DefaultGraphCell ){
							t = (DefaultGraphCell) graph.getSelectionCell();

							
							Transition transition = getTransition();
							
							DefaultEdge edge = new DefaultEdge(transition);
							
							edge.setSource(i.getChildAt(0));
							edge.setTarget(t.getChildAt(0));
							GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_SIMPLE);
							GraphConstants.setEndFill(edge.getAttributes(), true);
							GraphConstants.setEditable(edge.getAttributes(), false);
							GraphConstants.setSizeable(edge.getAttributes(), false);
							GraphConstants.setLineStyle(edge.getAttributes(), GraphConstants.STYLE_BEZIER);
							GraphConstants.setDisconnectable(edge.getAttributes(), false);
							GraphConstants.setLabelAlongEdge(edge.getAttributes(), true);
							
							
							graph.getGraphLayoutCache().insert(edge);
							cells.add(edge);					
							
							i=t=null;
							transicao=false;
							selectButton.setSelected(true);
						}
					}
				}
			}

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount() == 2)
					editarArestaAction();
			}
        	
        };
        
        
        keyListener = new DeletarEstadoAction(this);
        
    	group = new ButtonGroup();

        barraIcones = new javax.swing.JToolBar();
        selectButton = new javax.swing.JToggleButton();
        estadoNormalButton = new javax.swing.JToggleButton();
        estadoFinalButton = new javax.swing.JToggleButton();
        transicaoButton = new javax.swing.JToggleButton();
        scrollpane = new javax.swing.JScrollPane(graph);
        barraMenu = new javax.swing.JMenuBar();
        menuArquivo = new javax.swing.JMenu();
        novaItem = new javax.swing.JMenuItem();
        separador1 = new javax.swing.JSeparator();
        separador2 = new javax.swing.JSeparator();
        sairItem = new javax.swing.JMenuItem();
        menuMaquinaTuring = new javax.swing.JMenu();
        executarItem = new javax.swing.JMenuItem();
        simulacaoItem = new javax.swing.JMenuItem();
        abrirItem = new JMenuItem();
        salvarItem = new JMenuItem();
        
        alfabeto = new JList(new DefaultListModel());
        adicionarAlfabeto = new JButton("Adicionar");
        deletarAlfabeto = new JButton("Remover");
        
        scrollpaneAlfabeto = new JScrollPane(alfabeto);
        
        alfabetos = new JComboBox(new DefaultComboBoxModel(new String[]{"Alfabeto","Alfabeto Auxiliar"}));
        
        novaItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/new16x16.png")));
        abrirItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/open16x16.png")));
        salvarItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/save16x16.png")));
        sairItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/exit16x16.png")));
        executarItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/execute16x16.png")));
        simulacaoItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/simulate16x16.png")));
        estadoNormalButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/normal.png")));
        estadoFinalButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/final.png")));
        selectButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/select.gif")));
        transicaoButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("br/ufal/cpmat/tomate/view/icons/transition.gif")));
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        barraIcones.setFloatable(false);
        
        selectButton.setToolTipText("Selecionar");
        selectButton.setSelected(true);
        selectButton.setFocusable(false);
        selectButton.setMargin(new Insets(0,0,0,0));
        barraIcones.add(selectButton);

        estadoNormalButton.setToolTipText("Estado");
        estadoNormalButton.setFocusable(false);
        estadoNormalButton.setMargin(new Insets(0,0,0,0));
        barraIcones.add(estadoNormalButton);

        estadoFinalButton.setToolTipText("Estado Final");
        estadoFinalButton.setFocusable(false);
        estadoFinalButton.setMargin(new Insets(0,0,0,0));
        barraIcones.add(estadoFinalButton);

        transicaoButton.setToolTipText("Transição");
        transicaoButton.setFocusable(false);
        transicaoButton.setMargin(new Insets(0,0,0,0));
        barraIcones.add(transicaoButton);
        
        group.add(selectButton);
        group.add(estadoFinalButton);
        group.add(estadoNormalButton);
        group.add(transicaoButton);

        menuArquivo.setText("Arquivo");
        novaItem.setText("Nova Máquina");
        
        abrirItem.setText("Abrir");
        salvarItem.setText("Salvar");
        
        menuArquivo.add(novaItem);
        menuArquivo.add(abrirItem);
        menuArquivo.add(salvarItem);

        menuArquivo.add(separador1);

        sairItem.setText("Sair");
        menuArquivo.add(sairItem);

        barraMenu.add(menuArquivo);

        menuMaquinaTuring.setText("Máquina de Turing");
        executarItem.setText("Executar");
        menuMaquinaTuring.add(executarItem);

        simulacaoItem.setText("Simulação");
        menuMaquinaTuring.add(simulacaoItem);

        barraMenu.add(menuMaquinaTuring);

        setJMenuBar(barraMenu);
        
        simulacaoPanel.add(new JLabel("Conteúdo da fita:"));
        simulacaoPanel.add(fita);
        
        alfabetos.addActionListener(new MudarListaAlfabetoAction(this));
        adicionarAlfabeto.addActionListener(new AdicionarAlfabetoAction(this));
        deletarAlfabeto.addActionListener(new DeletarAlfabetoAction(this));
        executarItem.addActionListener(new ExecutarAction(this));
        simulacaoItem.addActionListener(new SimularAction(this));        
        nextStep.addActionListener(new ExecutarProximoPassoAction(this));
        pararButton.addActionListener(new PararSimulacaoAction(this));
        novaItem.addActionListener(new NovaAction(this));
        sairItem.addActionListener(new SairAction(this));
        
        salvarItem.addActionListener(new SalvarAction(this));
        abrirItem.addActionListener(new AbrirAction(this));
        
        
        //TODO ACTIONS MUDAR!
        

        this.setTitle("TOMaTE");
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addComponent(barraIcones, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
	            .addGroup(layout.createSequentialGroup()
	            		.addGroup( layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
	            				.addComponent(scrollpane, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
	            				.addComponent(simulacaoScrollPane))
	            		.addGroup( layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
	            				.addComponent(alfabetos,javax.swing.GroupLayout.PREFERRED_SIZE,150,150)
	            				.addComponent(scrollpaneAlfabeto,javax.swing.GroupLayout.PREFERRED_SIZE,150,150)
	            				.addComponent(adicionarAlfabeto,javax.swing.GroupLayout.PREFERRED_SIZE,150,150)
	            				.addComponent(deletarAlfabeto,javax.swing.GroupLayout.PREFERRED_SIZE,150,150)
	            				.addGap(5)
	            				.addComponent(separador2,javax.swing.GroupLayout.PREFERRED_SIZE,150,150)
	            				.addGap(5)
	            				.addComponent(simulacaoLabel)
	            				.addComponent(nextStep,javax.swing.GroupLayout.PREFERRED_SIZE,150,150)
	            				.addComponent(pararButton,javax.swing.GroupLayout.PREFERRED_SIZE,150,150)
	            				
	            		))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                .addComponent(barraIcones, javax.swing.GroupLayout.DEFAULT_SIZE, 17, 17)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                		.addGroup(layout.createSequentialGroup()
	                				.addComponent(scrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 326, Short.MAX_VALUE)
	                				.addComponent(simulacaoScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 100, 100))
	                		.addGroup(layout.createSequentialGroup()
	                				.addComponent(alfabetos,javax.swing.GroupLayout.PREFERRED_SIZE,25,25)
	                				.addComponent(scrollpaneAlfabeto,javax.swing.GroupLayout.PREFERRED_SIZE,100,100)
	                				.addComponent(adicionarAlfabeto)
	                				.addComponent(deletarAlfabeto)
	                				.addGap(5)
	                				.addComponent(separador2,javax.swing.GroupLayout.PREFERRED_SIZE,5,5)
	                				.addGap(5)
	                				.addComponent(simulacaoLabel)
	                				.addComponent(nextStep)
	                				.addComponent(pararButton)
	                				
	                		)))
	                
	        );
	        pack();
	        simulacaoLabel.setVisible(false);
	        nextStep.setVisible(false);
	        pararButton.setVisible(false);
	        simulacaoScrollPane.setVisible(false);
	        adicionarAlfabeto.setVisible(false);
	        deletarAlfabeto.setVisible(false);
	        selectButton.setEnabled(false);
	        estadoNormalButton.setEnabled(false);
	        estadoFinalButton.setEnabled(false);
	        transicaoButton.setEnabled(false);
	        alfabetos.setVisible(false);
	        scrollpaneAlfabeto.setVisible(false);
	        separador2.setVisible(false);
	        executarItem.setEnabled(false);
	        simulacaoItem.setEnabled(false);
	        salvarItem.setEnabled(false);
	        
	        
    }// </editor-fold>//GEN-END:initComponents
    
    protected void showSimulation() {
    	simulacaoLabel.setVisible(true);
        nextStep.setVisible(true);
        pararButton.setVisible(true);
        simulacaoScrollPane.setVisible(true);
        adicionarAlfabeto.setEnabled(false);
        deletarAlfabeto.setEnabled(false);
        graph.setEnabled(false);
        selectButton.setSelected(true);
        selectButton.setEnabled(false);
        estadoNormalButton.setEnabled(false);
        estadoFinalButton.setEnabled(false);
        transicaoButton.setEnabled(false);
        
        novaItem.setEnabled(false);
        executarItem.setEnabled(false);
        simulacaoItem.setEnabled(false);
    }
    
    public void editarArestaAction() {
		// TODO Auto-generated method stub
		if ( graph.getSelectionCell() instanceof DefaultEdge ) {
			Transition transition = getTransition();
			((DefaultEdge)graph.getSelectionCell()).setUserObject(transition);
			graph.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			graph.updateUI();
		}
	}
    
    public void deletarCelulaAction() {
    	// TODO Auto-generated method stub
    	Vector<Object> toDelete = new Vector<Object>();
    	for ( Object cell : graph.getSelectionCells() ) {
    		if ( cell instanceof DefaultEdge ) {
    			if ( !toDelete.contains(cell) ){
    				toDelete.add(cell);
    			}
    		}
    		else {
    			DefaultGraphCell vertex = (DefaultGraphCell)cell;
    			if ( !vertex.getAttributes().get("viewClassKey").equals("br.ufal.cpmat.tomate.view.cellview.JGraphInitialStateView") ){
    				toDelete.add(cell);
    				for ( Object edge : cells ) {
    					if ( edge instanceof DefaultEdge ) {
    						DefaultGraphCell source = (DefaultGraphCell)((DefaultPort)((DefaultEdge)edge).getSource()).getParent();
    						DefaultGraphCell target = (DefaultGraphCell)((DefaultPort)((DefaultEdge)edge).getTarget()).getParent();
    						if ( !toDelete.contains(edge)&&( (vertex.equals(source))||(vertex.equals(target)) ) )
    							toDelete.add(edge);
    					}
    				}
    			}
    		}
    	}
    	model.remove(toDelete.toArray());
    	cells.removeAll(toDelete);
    }
    
    public void mudarListaAlfabetoAction() {
		DefaultListModel model = ((DefaultListModel)alfabeto.getModel());
		if ( alfabetos.getSelectedIndex() == 1 ){
			alfabetoList.clear();
			int size = model.size();
			for ( int i=0;i<size;i++ ){
				alfabetoList.add((Character)model.get(i));
			}
			model.clear();
			for ( Character symbol : alfabetoAuxList ){
				model.addElement(symbol);
			}
		}
		else{
			alfabetoAuxList.clear();
			int size = model.size();
			for ( int i=0;i<size;i++ ){
				alfabetoAuxList.add((Character)model.get(i));
			}
			model.clear();
			for ( Character symbol : alfabetoList ){
				model.addElement(symbol);
			}
		}
	}
    
    public void salvarListaAlfabeto() {
		DefaultListModel model = ((DefaultListModel)alfabeto.getModel());
		if ( alfabetos.getSelectedIndex() == 0 ){
			alfabetoList.clear();
			int size = model.size();
			for ( int i=0;i<size;i++ ){
				alfabetoList.add((Character)model.get(i));
			}
		}
		else{
			alfabetoAuxList.clear();
			int size = model.size();
			for ( int i=0;i<size;i++ ){
				alfabetoAuxList.add((Character)model.get(i));
			}
		}
	}
    
    public void adicionarAlfabetoAction() {
		String adicionar;
		adicionar = JOptionPane.showInputDialog("Digite o carater (não pode ser '+' [símbolo branco] e '*' [símbolo de ínicio da fita]) ");
		if ( (adicionar!=null)&&(adicionar.length()>0)&&!((DefaultListModel)alfabeto.getModel()).contains(adicionar.charAt(0))&&(adicionar.charAt(0)!='*')&&(adicionar.charAt(0)!='+')&&(adicionar.charAt(0)!=' ') )
			((DefaultListModel)alfabeto.getModel()).addElement(adicionar.charAt(0));
		
	}
    
    public void deletarAlfabetoAction() {
		DefaultListModel model = (DefaultListModel)alfabeto.getModel();
		if ( alfabeto.getSelectedIndex() != -1 )
			model.remove(alfabeto.getSelectedIndex());
	}
    
    public void executarAction() {
		
    	String tape = JOptionPane.showInputDialog("Digite o conteudo da fita");

    	if ( tape != null ){

    		DefaultListModel model = ((DefaultListModel)alfabeto.getModel());
    		if ( alfabetos.getSelectedIndex() == 0 ){
    			alfabetoList.clear();
    			int size = model.size();
    			for ( int i=0;i<size;i++ ){
    				alfabetoList.add((Character)model.get(i));
    			}
    		}
    		else{
    			alfabetoAuxList.clear();
    			int size = model.size();
    			for ( int i=0;i<size;i++ ){
    				alfabetoAuxList.add((Character)model.get(i));
    			}
    		}


    		Collection<State> states = new ArrayList<State>();
    		Transitions transitions = new Transitions();
    		State initialState = null;
    		Collection<State> finalStates = new ArrayList<State>();

    		for ( Object cell : cells ) {
    			if ( cell instanceof DefaultEdge ){
    				DefaultEdge edge = (DefaultEdge)cell;
    				Transition transition = (Transition)edge.getUserObject();
    				transitions.put( new State(((DefaultPort)edge.getSource()).getParent().toString())
    				, transition.getReadSymbol()
    				, transition.getWriteSymbol()
    				, transition.getMovimentType()
    				, new State(((DefaultPort)edge.getTarget()).getParent().toString())
    				);
    			}
    			else {
    				DefaultGraphCell vertex = (DefaultGraphCell)cell;
    				states.add(new State(vertex.toString()));
    				if ( vertex.getAttributes().get("viewClassKey").equals("br.ufal.cpmat.tomate.view.cellview.JGraphFinalStateView") )
    					finalStates.add(new State(vertex.toString()));
    				else if ( vertex.getAttributes().get("viewClassKey").equals("br.ufal.cpmat.tomate.view.cellview.JGraphInitialStateView") )
    					initialState = new State(vertex.toString());
    			} 
    		}
    		Constructor c;
    		try {
    			c = modelClass.getConstructor(Collection.class,Collection.class,Transitions.class,State.class,Collection.class,Collection.class,Character.class,Character.class,ResultObserver.class);
    			machine = (TomateModelInterface)c.newInstance(alfabetoList,states,transitions,initialState,finalStates,alfabetoAuxList,'+','*',this);
    			machine.registerResultObserver(this);
    			machine.execute(tape);
    		} catch (SecurityException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (NoSuchMethodException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IllegalArgumentException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (InstantiationException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IllegalAccessException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (InvocationTargetException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
		
	}
    
    public void simularAction() {
    	
    	String tape = JOptionPane.showInputDialog("Digite o conteudo da fita");

    	if ( tape != null ){
    	
    	showSimulation();
        
        DefaultListModel model = ((DefaultListModel)alfabeto.getModel());
		if ( alfabetos.getSelectedIndex() == 0 ){
			alfabetoList.clear();
			int size = model.size();
			for ( int i=0;i<size;i++ ){
				alfabetoList.add((Character)model.get(i));
			}
		}
		else{
			alfabetoAuxList.clear();
			int size = model.size();
			for ( int i=0;i<size;i++ ){
				alfabetoAuxList.add((Character)model.get(i));
			}
		}
		
		Collection<State> states = new ArrayList<State>();
		Transitions transitions = new Transitions();
		State initialState = null;
		Collection<State> finalStates = new ArrayList<State>();
		
		for ( Object cell : cells ) {
			if ( cell instanceof DefaultEdge ){
				DefaultEdge edge = (DefaultEdge)cell;
				Transition transition = (Transition)edge.getUserObject();
				transitions.put( new State(((DefaultPort)edge.getSource()).getParent().toString())
								, transition.getReadSymbol()
								, transition.getWriteSymbol()
								, transition.getMovimentType()
								, new State(((DefaultPort)edge.getTarget()).getParent().toString())
								);
			}
			else {
				DefaultGraphCell vertex = (DefaultGraphCell)cell;
				states.add(new State(vertex.toString()));
				if ( vertex.getAttributes().get("viewClassKey").equals("br.ufal.cpmat.tomate.view.cellview.JGraphFinalStateView") )
					finalStates.add(new State(vertex.toString()));
				else if ( vertex.getAttributes().get("viewClassKey").equals("br.ufal.cpmat.tomate.view.cellview.JGraphInitialStateView") )
					initialState = new State(vertex.toString());
			} 
		}
		try {
			Constructor c= modelClass.getConstructor(Collection.class,Collection.class,Transitions.class,State.class,Collection.class,Collection.class,Character.class,Character.class,ResultObserver.class);
			machine = (TomateModelInterface)c.newInstance(alfabetoList,states,transitions,initialState,finalStates,alfabetoAuxList,'+','*',this);
			machine.registerResultObserver(this);
			machine.registerTapeObserver(this);
			machine.registerStateObserver(this);
			machine.setUp(tape);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
	}
    
    public void executarProximoPassoAction() {
    	machine.executeNextStep();
    }
    
    public void pararSimulacaoAction() {
    	machine = null;
    	fita.setTape("*", 0);
    	simulacaoLabel.setVisible(false);
        nextStep.setVisible(false);
        pararButton.setVisible(false);
        simulacaoScrollPane.setVisible(false);
        adicionarAlfabeto.setEnabled(true);
        deletarAlfabeto.setEnabled(true);
        graph.setEnabled(true);
        selectButton.setEnabled(true);
        estadoNormalButton.setEnabled(true);
        estadoFinalButton.setEnabled(true);
        transicaoButton.setEnabled(true);
        novaItem.setEnabled(true);
        executarItem.setEnabled(true);
        simulacaoItem.setEnabled(true);
    }
    
    protected Transition getTransition() {
		// TODO Auto-generated method stub
    	String read,write,moviment;
    	read = JOptionPane.showInputDialog("Símbolo lido da fita");
    	while ( (read == null)||(read.isEmpty()) ) {
    		read = JOptionPane.showInputDialog("Símbolo lido da fita");
    	}
		write = JOptionPane.showInputDialog("Símbolo gravado na fita");
		while ( (write == null)||(write.isEmpty()) ) {
			write = JOptionPane.showInputDialog("Símbolo gravado na fita");
    	}
		moviment = JOptionPane.showInputDialog("Movimento do cabeçote ( D para direita ou E para esquerda)");
		while ( (moviment == null)||(moviment.isEmpty())||( (Character.toUpperCase(moviment.charAt(0)) != 'D')&&(Character.toUpperCase(moviment.charAt(0)) != 'E') ) ) {
			moviment = JOptionPane.showInputDialog("Movimento do cabeçote ( D para direita ou E para esquerda)");
    	}
		MovimentType type;
		if ( Character.toUpperCase(moviment.charAt(0)) == 'E' )
			type = MovimentType.LEFT;
		else
			type = MovimentType.RIGHT;
		return new Transition(read.charAt(0),write.charAt(0),type);
	}
	
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraIcones;
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JToggleButton selectButton;
    private javax.swing.JToggleButton estadoFinalButton;
    private javax.swing.JToggleButton estadoNormalButton;
    private javax.swing.JMenuItem executarItem;
    private javax.swing.JScrollPane scrollpane;
    private javax.swing.JMenu menuArquivo;
    private javax.swing.JMenu menuMaquinaTuring;
    private javax.swing.JMenuItem novaItem;
    private javax.swing.JMenuItem sairItem;
    private javax.swing.JSeparator separador1;
    private javax.swing.JSeparator separador2;
    private javax.swing.JMenuItem simulacaoItem;
    private javax.swing.JToggleButton transicaoButton;
    private JList alfabeto;
    private JScrollPane scrollpaneAlfabeto;
    private JButton adicionarAlfabeto;
    private JButton deletarAlfabeto;
    private JComboBox alfabetos;
    private ButtonGroup group;
    // End of variables declaration//GEN-END:variables
    
    private JLabel simulacaoLabel = new JLabel("Simulação");
    private JButton nextStep = new JButton("Próximo passo");
    private JButton pararButton = new JButton("Parar");
    
    private FitaPanel fita = new FitaPanel();
    private JPanel simulacaoPanel = new JPanel();
    
    private JScrollPane simulacaoScrollPane = new JScrollPane(simulacaoPanel);
    
    private Collection<Character> alfabetoList = new Vector<Character>();
    private Collection<Character> alfabetoAuxList = new Vector<Character>();
    private JGraph graph;
    private GraphModel model;
    private GraphLayoutCache view;
    private List<Object> cells;
    private int index=1;    
    
    private MouseListener mouseListener;
    private KeyListener keyListener;
    private GraphLayoutCacheListener[] graphListener;
    
    private Class<?> modelClass;
    private TomateModelInterface machine;
    
    private JMenuItem abrirItem;
    private JMenuItem salvarItem;

	public void showResult() {
		// TODO Auto-generated method stub
		Result result = machine.getResult();
		JOptionPane.showMessageDialog(null, result.getMessage());
		pararSimulacaoAction();
	}

	public void updateState() {
		// TODO Auto-generated method stub
		setSelected(machine.getActualState());
	}

	public void updateTape() {
		// TODO Auto-generated method stub
		fita.setTape(machine.tapeContent(),machine.getMarker());
	}

	public void novaAction() {
		// TODO Auto-generated method stub
		newMachine();
		DefaultGraphCell newCell = createVertex(20, 20, 50, 50, null, false, new DefaultGraphCell("q0"), "br.ufal.cpmat.tomate.view.cellview.JGraphInitialStateView");
		graph.getGraphLayoutCache().insert(newCell);
		cells.add(newCell);
		executarItem.setEnabled(true);
        simulacaoItem.setEnabled(true);
        salvarItem.setEnabled(true);
	}

	public void sairAction() {
		// TODO Auto-generated method stub
		dispose();
	}

	public void showView() {
		// TODO Auto-generated method stub
		setVisible(true);
	}

	public void hideView() {
		// TODO Auto-generated method stub
		setVisible(false);
	}

	@Override
	public void salvarAction() {
		// TODO Auto-generated method stub
		XStream stream = new XStream();
		
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(this);

	      if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        try {
				FileOutputStream output = new FileOutputStream(file);
				TuringMachine machine = new TuringMachine();
				uninstallListeners();
				machine.setView(this.graph.getGraphLayoutCache());
				salvarListaAlfabeto();
				machine.setAlfabetoAuxList(this.alfabetoAuxList);
				machine.setAlfabetoList(this.alfabetoList);
				machine.setCells(this.cells);
				machine.setIndex(index);
				machine.setMachine(this.machine);
				String xml = "<?xml version='1.0' encoding='UTF-8' ?>\n"+stream.toXML(machine);
				installListeners();
				output.write(xml.getBytes());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
		
	}
	
	@Override
	public void abrirAction() {
		// TODO Auto-generated method stub
		XStream stream = new XStream();
		
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(this);

	      if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        try {
				FileInputStream input = new FileInputStream(file);
				TuringMachine machine = (TuringMachine)stream.fromXML(input);
				newMachine(machine);
				executarItem.setEnabled(true);
		        simulacaoItem.setEnabled(true);
		        salvarItem.setEnabled(true);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
		
	}
}