package com.fi.slot.book;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class SlotUpdateBot extends TelegramLongPollingBot {
    private List<String> chatIds= new ArrayList<>();
    private static final String BOT_USERNAME = "@CheckF1Availability_bot";
    private static final String BOT_TOKEN = "7056918317:AAEdlIAa_o4V1I43g_BEAJdZPlQpvN_aG5s";
    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Long chatId = update.getMessage().getChatId();
            System.out.println("ChatIds"+ update.getMessage().getFrom() +": "+ update.getMessage().getChatId());
            chatIds.add(chatId.toString());
        }
    }

    public void sendSlotUpdate(String message){
        for(String chatId: chatIds){
            SendMessage sendMessage= new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.enableHtml(true);
            sendMessage.setText(message);
            try{
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
