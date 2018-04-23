package br.com.filmes.controller;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.pengrad.telegrambot.model.Update;

public interface FilmeController {
	
	public void pesquisar(Update update) throws FileNotFoundException, IOException;
}
