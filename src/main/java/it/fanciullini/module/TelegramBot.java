package it.fanciullini.module;

import it.fanciullini.data.entity.User;
import it.fanciullini.data.service.UserService;
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

    public TelegramBot(String token, UserService userService){
        this.token = token;
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        SendMessage message = new SendMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String request = update.getMessage().getText().toLowerCase();
            SendMessage sendMessage = new SendMessage();
            switch(request){
                case "/start":
                    api_start(update);
                    break;
                case "/setUserByNameAndSurname":
                    api_profile(update);
                    break;
                default:
                    api_default(update);
                    break;
            }
        }
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage api_start(Update update){
        checkChatId(update.getMessage());
        return new SendMessage();
    }

    private SendMessage api_profile(Update update){
        checkChatId(update.getMessage());
        return new SendMessage();
    }

    private SendMessage api_default(Update update){
        return new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(update.getMessage().getChatId())
                .setText(warningMessage);
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

    private void checkChatId(Message message){
        User user = userService.findByTelegramId(message.getChatId());
        if (user == null){
            user = selectOrCreate(message.getFrom().getFirstName(), message.getFrom().getLastName(), message.getChatId());
        }
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
