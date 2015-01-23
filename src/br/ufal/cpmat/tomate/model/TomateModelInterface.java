package br.ufal.cpmat.tomate.model;

import br.ufal.cpmat.tomate.view.observers.ResultObserver;
import br.ufal.cpmat.tomate.view.observers.StateObserver;
import br.ufal.cpmat.tomate.view.observers.TapeObserver;

public interface TomateModelInterface{

	void setUp(String tape);
	void execute(String tape);
	boolean executeNextStep();
	String tapeContent();
	String getActualState();
	int getMarker();
	Result getResult();
	
	void registerResultObserver(ResultObserver o);
	void registerStateObserver(StateObserver o);
	void registerTapeObserver(TapeObserver o);
	
	void removeResultObserver(ResultObserver o);
	void removeStateObserver(StateObserver o);
	void removeTapeObserver(TapeObserver o);
	
	void removeAllObservers();
	
}
