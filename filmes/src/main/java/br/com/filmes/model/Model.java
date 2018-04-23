package br.com.filmes.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Update;

import br.com.filmes.FilmeExemplo;
import br.com.filmes.view.Observer;

public class Model {

	private List<Observer> observers = new LinkedList<Observer>();
	private static Model uniqueInstance;

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObservers(long chatId, String studentsData) {
		for (Observer observer : observers) {
			observer.update(chatId, studentsData);
		}
	}

	public void listaFilmesAleatorio(Update update) throws FileNotFoundException, IOException {
		try(Reader reader = new FileReader("C:\\Users\\tsoar\\Desktop\\filmes\\src\\main\\java\\br\\com\\filmes\\model\\filmes.txt")){
			
			Gson gson 			  = new Gson();
			FilmeExemplo[] filmes = gson.fromJson(reader, FilmeExemplo[].class);
			ArrayList<FilmeExemplo>filmesAleatorio = new ArrayList<>(Arrays.asList(filmes));
			this.notifyObservers(update.message().chat().id(), imprimirFilmeAleatorio(filmesAleatorio));
		}
	}
	
	public void listaFilmesPorGenero(Update update) throws FileNotFoundException, IOException {
			try(Reader reader = new FileReader("C:\\Users\\tsoar\\Desktop\\filmes\\src\\main\\java\\br\\com\\filmes\\model\\filmes.txt")){
			
			Gson gson 			  = new Gson();
			FilmeExemplo[] filmes = gson.fromJson(reader, FilmeExemplo[].class);
			ArrayList<FilmeExemplo> filmesGenero = new ArrayList<>(Arrays.asList(filmes));
			filmesGenero.clear();
			for (FilmeExemplo f : filmes) {
				if(update.message().text().toUpperCase().equals(f.getGenero().toUpperCase())) {
					filmesGenero.add(f);
				}
			}
			this.notifyObservers(update.message().chat().id(), imprimirFilmes(filmesGenero));
		}
	}
	
	
	
	public String imprimirFilmeAleatorio(ArrayList<FilmeExemplo> filmes) {
		Random gerador 		= new Random();
		int numero 			= gerador.nextInt(filmes.size());
		String imprimessao 	= "";
		FilmeExemplo f 		= filmes.get(numero);
			
		imprimessao += "Ranking : " + f.getRank() 	+ "\n" 
					+  "Nome : " 	+ f.getNome() 	+ "\n\n"; 
				//	+  "Imagem : " 	+ f.getImagem() + "\n\n";
		return imprimessao;
	}


	public String imprimirFilmes(ArrayList<FilmeExemplo> filmes) {
		String imprimessao 	= "";
		
		for (FilmeExemplo f : filmes) {
			imprimessao += "Ranking : " + f.getRank() 	+ "\n" 
					+  "Nome : " 	+ f.getNome() 	+ "\n\n"; 
					//+  "Imagem : " 	+ f.getImagem() + "\n\n";
		}
		if(!filmes.isEmpty())
			return imprimessao;
		else
			return "Nenhum resultado encontrado para esse gênero";
	}
	
}


