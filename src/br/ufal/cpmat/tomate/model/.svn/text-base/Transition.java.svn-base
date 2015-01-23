package br.ufal.cpmat.tomate.model;

public class Transition {
	private TransitionRequisition requisition;
	private TransitionAction action;
	
	public Transition ( State readState, Character readSymbol, Character writeSymbol, MovimentType moviment, State nextState ) {
		this.requisition = new TransitionRequisition(readState,readSymbol);
		this.action = new TransitionAction(writeSymbol,moviment,nextState);
	}
	
	public State readState() {
		return this.requisition.state();
	}
	
	public Character readSymbol() {
		return this.requisition.symbol();
	}
	
	public Character writeSymbol() {
		return this.action.symbol();
	}
	
	public MovimentType moviment() {
		return this.action.moviment();
	}
	
	public State nextState() {
		return this.action.state();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((action == null) ? 0 : action.hashCode());
		result = PRIME * result + ((requisition == null) ? 0 : requisition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Transition other = (Transition) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (requisition == null) {
			if (other.requisition != null)
				return false;
		} else if (!requisition.equals(other.requisition))
			return false;
		return true;
	}
	
	public String toString() {
		return String.format("( %s, %s, %s, %s, %s )", this.requisition.state(), 
				this.requisition.symbol(), this.action.symbol(), this.action.moviment(),
				this.action.state() );
	}
	
}
