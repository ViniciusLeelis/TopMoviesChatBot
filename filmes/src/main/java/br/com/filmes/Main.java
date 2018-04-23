package br.com.filmes;

import java.io.FileNotFoundException;
import java.io.IOException;

import br.com.filmes.model.Model;
import br.com.filmes.view.View;

public class Main {

	private static Model model;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		model = Model.getInstance();
		
		View view = new View(model);
		model.registerObserver(view); //connection Model -> View
		view.receiveUsersMessages();
	}

}
