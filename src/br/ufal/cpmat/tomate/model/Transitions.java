package br.ufal.cpmat.tomate.model;

import java.util.HashMap;

public class Transitions {
	
	private HashMap<TransitionRequisition, TransitionAction> transitions;
	
	public Transitions() {
		this.transitions = new HashMap<TransitionRequisition, TransitionAction>();
	}
	
	public void put ( State actualState, Character readSymbol, Character writeSymbol, MovimentType moviment, State nextState ) {
		TransitionRequisition requisition = new TransitionRequisition( actualState, readSymbol );
		TransitionAction action = new TransitionAction( writeSymbol,moviment,nextState );
		
		this.transitions.put(requisition, action);
	}
	
	public TransitionAction get ( State actualState, Character readSymbol ) {
		TransitionRequisition requisition = new TransitionRequisition(actualState,readSymbol);
		return this.transitions.get(requisition);
	}
	
}
