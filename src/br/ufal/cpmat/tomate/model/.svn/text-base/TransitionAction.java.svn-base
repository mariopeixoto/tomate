package br.ufal.cpmat.tomate.model;

public class TransitionAction {

	private Character symbol;
	private MovimentType moviment;
	private State state;
	
	public TransitionAction( 	Character writeSymbol,
								MovimentType moviment,
								State nextState ) {
		
		this.symbol = writeSymbol;
		this.moviment = moviment;
		this.state = nextState;
		
	}
	
	public MovimentType moviment() {
		return this.moviment;
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
		result = PRIME * result + ((moviment == null) ? 0 : moviment.hashCode());
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
		final TransitionAction other = (TransitionAction) obj;
		if (moviment == null) {
			if (other.moviment != null)
				return false;
		} else if (!moviment.equals(other.moviment))
			return false;
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
