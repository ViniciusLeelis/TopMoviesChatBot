package br.com.filmes.model;

import br.com.filmes.view.Observer;

public interface Subject {
	public void registerObserver(Observer observer);
	public void notifyObservers(long chatId, String data);
}
