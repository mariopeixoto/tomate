package br.ufal.cpmat.tomate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import br.ufal.cpmat.tomate.view.observers.ResultObserver;
import br.ufal.cpmat.tomate.view.observers.StateObserver;
import br.ufal.cpmat.tomate.view.observers.TapeObserver;

public class TuringMachine implements TomateModelInterface,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8963041567447057186L;

	private transient Tape tape;
	
	private Collection<Character> alphabet;
	private Collection<State> states;//Conjunto de estados possíveis
	private Transitions transitions;//Função de transição
	private State initialState;//Estado inicial da máquina
	private Collection<State> finalStates;//Conjunto de estados finais
	private Collection<Character> auxiliarAlphabet;
	private Character blankChar;
	private Character initChar;
	
	private transient State actualState;
	
	private transient Result result = null;
	
	private transient ArrayList<ResultObserver> resultObservers = new ArrayList<ResultObserver>();
	private transient ArrayList<TapeObserver> tapeObservers = new ArrayList<TapeObserver>();
	private transient ArrayList<StateObserver> stateObservers = new ArrayList<StateObserver>();
	
	public TuringMachine(Collection<Character> alphabet , Collection<State> states ,
						Transitions transitions , State initialState ,
						Collection<State> finalStates , Collection<Character> auxiliarAlphabet ,
						Character blankChar, Character initChar, ResultObserver parent ){
		//TODO Verificar se os estados em transitions fazem parte do conjunto de estados
		
		if ( !states.contains(initialState) || !states.containsAll(finalStates) ){
			result = new NotAValidMachineException();
			parent.showResult();
			
		} else {
			this.alphabet = alphabet;
			this.states = states;
			this.transitions = transitions;
			this.initialState = initialState;
			this.finalStates = finalStates;
			this.auxiliarAlphabet = auxiliarAlphabet;
			this.blankChar = blankChar;
			this.initChar = initChar;

			this.tape = new Tape(blankChar,initChar);
		}
	}
	
	public TuringMachine() {
		
	}
	
	public Collection<Character> getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(Collection<Character> alphabet) {
		this.alphabet = alphabet;
	}

	public Collection<State> getStates() {
		return states;
	}

	public void setStates(Collection<State> states) {
		this.states = states;
	}

	public Transitions getTransitions() {
		return transitions;
	}

	public void setTransitions(Transitions transitions) {
		this.transitions = transitions;
	}

	public State getInitialState() {
		return initialState;
	}

	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	public Collection<State> getFinalStates() {
		return finalStates;
	}

	public void setFinalStates(Collection<State> finalStates) {
		this.finalStates = finalStates;
	}

	public Collection<Character> getAuxiliarAlphabet() {
		return auxiliarAlphabet;
	}

	public void setAuxiliarAlphabet(Collection<Character> auxiliarAlphabet) {
		this.auxiliarAlphabet = auxiliarAlphabet;
	}

	public Character getBlankChar() {
		return blankChar;
	}

	public void setBlankChar(Character blankChar) {
		this.blankChar = blankChar;
	}

	public Character getInitChar() {
		return initChar;
	}

	public void setInitChar(Character initChar) {
		this.initChar = initChar;
	}

	public void setUp(String tape) {
		this.actualState = this.initialState;
		notifyStateObservers();
		this.tape.write(tape);
		for ( int i=0;i<tape.length();i++ ) {
			if ( !alphabet.contains(tape.charAt(i)) ) {
				result = new NotAValidTapeContentException();
				notifyResultObservers();
				return;
			}
		}
		notifyTapeObservers();
	}
	
	public String getActualState() {
		return actualState.identifier();
	}
	
	public int getMarker() {
		return tape.getMarker();
	}
	
	public void execute(String tape) {
		ArrayList<TapeObserver> tapeObservers = new ArrayList<TapeObserver>(this.tapeObservers);
		ArrayList<StateObserver> stateObservers = new ArrayList<StateObserver>(this.stateObservers);
		
		this.tapeObservers.clear();
		this.stateObservers.clear();
		
		result = null;
		
		setUp(tape);
		if ( result == null )
			while ( executeNextStep() );
		
		this.tapeObservers = tapeObservers;
		this.stateObservers = stateObservers;
	}
	
	public boolean executeNextStep() {
		TransitionAction action = this.transitions.get(actualState, this.tape.read());
		if ( finalStates.contains(actualState) ) {
			result = new AcceptedException(this.tapeContent());
			notifyResultObservers();
			return false;
		}
		if ( action == null ) {
			result = new NotAcceptedException(this.tapeContent());
			notifyResultObservers();
			return false;
		}
		if ( !(alphabet.contains(action.symbol()) || auxiliarAlphabet.contains(action.symbol()) || action.symbol().equals(initChar) || action.symbol().equals(blankChar)) ) {
			result = new InvalidSymbolException(action.symbol());
			notifyResultObservers();
			return false;
		}
		this.tape.write(action.symbol());
		if ( action.moviment().equals(MovimentType.LEFT) ) {
			try {
				this.tape.moveLeft();
			}
			catch ( CantMoveLeftException exc ) {
				result = new NotAcceptedException(this.tapeContent());
				notifyResultObservers();
				return false;
			}
		}
		else {
				this.tape.moveRight();
		}
		notifyTapeObservers();
		State aux = actualState;
		actualState = action.state();
		if ( !actualState.equals(aux) )
			notifyStateObservers();
		return true;
	}

	private void notifyTapeObservers() {
		// TODO Auto-generated method stub
		for ( TapeObserver o : tapeObservers ) {
			o.updateTape();
		}
	}

	private void notifyStateObservers() {
		// TODO Auto-generated method stub
		for ( StateObserver o : stateObservers ) {
			o.updateState();
		}
	}

	private void notifyResultObservers() {
		// TODO Auto-generated method stub
		for ( ResultObserver o : resultObservers ) {
			o.showResult();
		}
	}

	public String tapeContent() {
		return this.tape.toString();
	}
	
	public Collection<Character> alphabet() {
		return this.alphabet;
	}
	
	public Collection<Character> auxiliarAlphabet() {
		return this.auxiliarAlphabet;
	}
	
	public Collection<State> states() {
		return this.states;
	}
	
	public Collection<State> finalStates(){
		return this.finalStates;
	}
	
	public State initialState() {
		return this.initialState;
	}
	
	public Character blankChar() {
		return this.blankChar;
	}
	
	public Character initChar() {
		return this.initChar;
	}
	
	public Collection<Transition> transitionFunction() {
		Collection<Transition> transitions = new ArrayList<Transition>();
		
		for ( State state : states ) {
			for ( Character character : alphabet ) {
				TransitionAction action = this.transitions.get(state, character);
				if ( action != null ) {
					transitions.add ( new Transition ( state,character, action.symbol(),
													action.moviment(), action.state() ) );
				}
			}
			for ( Character character : auxiliarAlphabet ) {
				TransitionAction action = this.transitions.get(state, character);
				if ( action != null ) {
					transitions.add ( new Transition ( state,character, action.symbol(),
													action.moviment(), action.state() ) );
				}
			}
			TransitionAction action = this.transitions.get(state, this.blankChar);
			if ( action != null ) {
				transitions.add ( new Transition ( state,this.blankChar, action.symbol(),
												action.moviment(), action.state() ) );
			}
			action = this.transitions.get(state, this.initChar);
			if ( action != null ) {
				transitions.add ( new Transition ( state,this.initChar, action.symbol(),
												action.moviment(), action.state() ) );
			}
		}
		
		return transitions;
		
	}

	public void registerResultObserver(ResultObserver o) {
		// TODO Auto-generated method stub
		resultObservers.add(o);
	}

	public void registerStateObserver(StateObserver o) {
		// TODO Auto-generated method stub
		stateObservers.add(o);
	}

	public void registerTapeObserver(TapeObserver o) {
		// TODO Auto-generated method stub
		tapeObservers.add(o);
	}

	public void removeResultObserver(ResultObserver o) {
		// TODO Auto-generated method stub
		if ( resultObservers.contains(o) )
			resultObservers.remove(o);
	}

	public void removeStateObserver(StateObserver o) {
		// TODO Auto-generated method stub
		if ( stateObservers.contains(o) )
			stateObservers.remove(o);
	}

	public void removeTapeObserver(TapeObserver o) {
		// TODO Auto-generated method stub
		if ( tapeObservers.contains(o) )
			tapeObservers.remove(o);
	}
	
	public Result getResult() {
		return this.result;
	}

	public void removeAllObservers() {
		// TODO Auto-generated method stub
		this.tapeObservers.clear();
		this.stateObservers.clear();
		this.resultObservers.clear();
	}
	
}
