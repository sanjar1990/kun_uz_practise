package com.example.utility;

import com.example.enums.Language;
import com.example.exceptions.AppBadRequestException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CheckValidationUtility {
    public String checkForPhone(String phone, Language language) {
        if(!phone.substring(1).chars().allMatch(Character::isDigit)){
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("i18n/messages/message");
            throw new AppBadRequestException(messageSource.getMessage("invalid.phone.number",null, new Locale(language.name())));
        }
        if (phone.length()==13 && phone.startsWith("+998")) {
            return phone;
        }
        if(phone.length()==9&&!phone.startsWith("+998") ){
            return "+998"+phone;
        }
        throw new AppBadRequestException("invalid phone number");
    }

    public void checkForPassword(String password){
        if(password.length()<6)throw new AppBadRequestException("Password length should be at least 6");
    }
}
