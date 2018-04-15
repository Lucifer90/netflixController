package it.fanciullini.module;

import it.fanciullini.data.entity.User;
import it.fanciullini.data.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot
{
    private String token;
    private UserService userService;
    private String warningMessage = "Questo boot non è ancora interattivo, per adesso funziona da dispatcher di notifiche.";

    private static Logger logger = LogManager.getLogger();

    public TelegramBot(String token, UserService userService){
        this.token = token;
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        SendMessage message = new SendMessage();
        String response = "";
        Long chatId = -1L;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String[] requestFull = update.getMessage().getText().toLowerCase().split(" ");
            String request = requestFull[0];
            chatId = update.getMessage().getChatId();
            switch(request){
                case "/start":
                    response = apiAddAtStart();
                    break;
                case "/help":
                    response = apiHelp();
                    break;
                case "/setbyanag":
                    if (requestFull.length < 3) {
                        response = "Errato numero di parametri, la sintassi è: /setByAnag nome cognome";
                    } else {
                        response = apiAddByAnag(update, requestFull[1], requestFull[2]);
                    }
                    break;
                case "/setbyusername":
                    if (requestFull.length < 2) {
                        response = "Errato numero di parametri, la sintassi è: /setByAnag username";
                    } else {
                        response = apiAddByUsername(update, requestFull[1]);
                    }
                    break;
                default:
                    response = apiDefaultResponse();
                    break;
            }
        }
        try {
            if (chatId != -1 && !response.isEmpty()) {
                message.setChatId(chatId);
                message.setText(response);
                execute(message); // Call method to send the message
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String apiAddAtStart(){
        return apiHelp();
    }

    private String apiAddByAnag(Update update, String name, String surname){
        return connectByAnag(name, surname, update.getMessage().getChatId());
    }

    private String apiAddByUsername(Update update, String username){
        return connectByUsername(username, update.getMessage().getChatId());
    }

    private String apiDefaultResponse(){
        return warningMessage;
    }

    private String apiHelp(){
        return "Ciao! Questo bot serve solo ed unicamente come dispatcher delle notifiche dell'applicazione\n" +
                "Netflix Controller! Per adesso supporta solamente i seguenti comandi:\n" +
                "1- /setByAnag nome cognome: Collega il tuo account NetflixController al tuo account telegram attravero nome e cognome\n" +
                "2- /setByUsername username: Collega il tuo account NetflixController al tuo account telegram attraverso la tua login al servizio\n";
    }

    public void sendNotification(Long chatId, String text){
        SendMessage message = new SendMessage().setChatId(chatId).setText(text);
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "NetflixController";
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private String checkChatId(Message message){
        User user = userService.findByTelegramId(message.getChatId());
        if (user == null){
            user = selectOrCreate(message.getFrom().getFirstName(), message.getFrom().getLastName(), message.getChatId());
            return "OK - Utente associato";
        } else {
            return "OK - Utente precedentemente associato";
        }
    }

    private String connectByAnag(String name, String surname, Long chatId){
        User user = userService.findByNameAndSurname(name, surname);
        user.setTelegramId(chatId);
        userService.save(user);
        return "OK - Utente associato";
    }

    private String connectByUsername(String  username, Long chatId){
        User user = userService.findByUserName(username);
        user.setTelegramId(chatId);
        userService.save(user);
        return "OK - Utente associato";
    }

    private User selectOrCreate(String name, String surname, Long chatId){
        User user = userService.findByNameAndSurname(name, surname);
        if (user != null){
            user.setTelegramId(chatId);
            userService.save(user);
        }
        return user;
    }

}
