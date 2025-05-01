package com.example.service;

import com.example.dto.sms.SmsDTO;
import com.example.enums.SmsType;
import com.example.exceptions.AppBadRequestException;
import com.example.utility.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Service
public class SmsSenderService {
    @Autowired
    private SmsHistoryService smsHistoryService;
    @Autowired
    private RestTemplate restTemplate;
    private final int smsLimitCount = 3;
    private final String url = "https://api.dasturjon.uz/api/v1/sms-provider/send";

    public void sendSmsVerification(String phone) {
   boolean result= smsHistoryService.getLimitCountLastTwoMinutes(phone)>=smsLimitCount;
    if(result) throw new AppBadRequestException("Sms limit reached:"+phone);

    String smsCode= RandomUtil.getRandomString();
        String message= "<#>kitabu.uz raqamni o'zgartirish tasdiqlash kodi: \n" + smsCode+"\n"+"signature";;
        send(phone,message,smsCode);
    smsHistoryService.save(phone,smsCode,message, SmsType.REGISTRATION);
    }

    private void send(String phone, String message, String smsCode) {
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        SmsDTO smsDTO=new SmsDTO();
        smsDTO.setPhone(phone);
        smsDTO.setMessage(message);
        smsDTO.setProjectName("kitobjon2");
        smsDTO.setCode(smsCode);
        HttpEntity<SmsDTO> httpEntity=new HttpEntity<>(smsDTO, headers);
        try {
String answer=restTemplate.postForObject(url, httpEntity, String.class);
            System.out.println(answer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
