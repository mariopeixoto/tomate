package br.ufal.cpmat.tomate.view;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

import br.ufal.cpmat.tomate.model.TomateModelInterface;

public class TuringMachine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4753256068841683790L;

	private GraphLayoutCache view;
	private Collection<Character> alfabetoList = new Vector<Character>();
    private Collection<Character> alfabetoAuxList = new Vector<Character>();
    private List<Object> cells;
    private int index;
    private TomateModelInterface machine;
    
	public GraphLayoutCache getView() {
		return view;
	}
	public void setView(GraphLayoutCache graph) {
		this.view = graph;
	}
	public Collection<Character> getAlfabetoList() {
		return alfabetoList;
	}
	public void setAlfabetoList(Collection<Character> alfabetoList) {
		this.alfabetoList = alfabetoList;
	}
	public List<Object> getCells() {
		return cells;
	}
	public void setCells(List<Object> cells) {
		this.cells = cells;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public TomateModelInterface getMachine() {
		return machine;
	}
	public void setMachine(TomateModelInterface machine) {
		this.machine = machine;
	}
	public Collection<Character> getAlfabetoAuxList() {
		return alfabetoAuxList;
	}
	public void setAlfabetoAuxList(Collection<Character> alfabetoAuxList) {
		this.alfabetoAuxList = alfabetoAuxList;
	}
    
    
	
}
