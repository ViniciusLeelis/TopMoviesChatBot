package br.com.filmes.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import br.com.filmes.controller.FilmeController;
import br.com.filmes.controller.ListaFilmePorGeneroController;
import br.com.filmes.controller.ListarFilmesAleatoriosController;
import br.com.filmes.model.Model;

public class View implements Observer{
	

	TelegramBot bot = TelegramBotAdapter.build("");

	//Object that receives messages
	GetUpdatesResponse updatesResponse;
	//Object that send responses
	SendResponse sendResponse;
	//Object that manage chat actions like "typing action"
	BaseResponse baseResponse;
			
	
	int queuesIndex			=0;
	boolean searchBehaviour = false;
	
	FilmeController filmeController; //Strategy Pattern -- connection View -> Controller
	private Model model;
	
	public View(Model model){
		this.model = model; 
	}
	
	public void setControllerSearch(FilmeController filmeController){ //Strategy Pattern
		this.filmeController = filmeController;
	}
	
	public void receiveUsersMessages() throws FileNotFoundException, IOException {
		
		//infinity loop
		while (true){
		
			//taking the Queue of Messages
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(queuesIndex));
			
			//Queue of messages
			List<Update> updates = updatesResponse.updates();

			//taking each message in the Queue
			for (Update update : updates) {
				executa(update);
			}
		}
	}
	

	public void executa(Update update) throws FileNotFoundException, IOException {
		//updating queue's index
		queuesIndex = update.updateId()+1;
		
		
		if(this.searchBehaviour==true){
			this.callController(update);
			
		} else if (update.message().text().equals("listar filmes aleatorios")) {
			setControllerSearch(new ListarFilmesAleatoriosController(model, this));
			this.searchBehaviour 	= true;
			this.callController(update);
		} else if (update.message().text().equals("listar filmes por genero")) {
			sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
					"Escolha uma das opções abaixo :" ).replyMarkup(new ReplyKeyboardMarkup(
							new String[] {"Comédia", "Terror"}, 
							new String[] {"Ação", "Romance"},
							new String[] {"Drama", "Suspense"})));
			
		}else if (update.message().text().equals("Comédia") 	|| update.message().text().equals("Terror") || 
				  update.message().text().equals("Ação")  || update.message().text().equals("Romance") ||
				  update.message().text().equals("Drama") 	|| update.message().text().equals("Suspense") ){
			
			setControllerSearch(new ListaFilmePorGeneroController(model, this));
			
			this.searchBehaviour 	= true;
			this.callController(update);
			
		}else {
			Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(new String[] { "listar filmes aleatorios", "listar filmes por genero" })
					.oneTimeKeyboard(false)
					.resizeKeyboard(true)
					.selective(true);
			bot.execute(new SendMessage(update.message().chat().id(), "Escolha uma das opções abaixo: ")
					.replyMarkup(replyKeyboardMarkup));
		}
	}
	
	
	public void callController(Update update) throws FileNotFoundException, IOException{
		this.filmeController.pesquisar(update);
	}
	
	public void update(long chatId, String studentsData){
		sendResponse 		 = bot.execute(new SendMessage(chatId, studentsData));
		this.searchBehaviour = false;
	}
	
	public void sendTypingMessage(Update update){
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

}
