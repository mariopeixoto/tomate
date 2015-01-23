package br.ufal.cpmat.tomate.model;

public class TransitionRequisition {

	private State state;
	private Character symbol;
	
	public TransitionRequisition ( State state, Character symbol ) {
		this.state = state;
		this.symbol = symbol;
	}
	
	public State state() {
		return this.state;
	}
	
	public Character symbol() {
		return this.symbol;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((state == null) ? 0 : state.hashCode());
		result = PRIME * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		final TransitionRequisition other = (TransitionRequisition) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	
}
