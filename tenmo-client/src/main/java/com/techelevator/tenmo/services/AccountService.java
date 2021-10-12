package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class AccountService {
    public static String AUTH_TOKEN = "";
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user1;



    public AccountService(String url, AuthenticatedUser user1){
        this.user1 = user1;
        baseUrl = url;
    }
    //Get the user account
    public Account getOneAccount(int id, AuthenticatedUser user1)throws AccountServiceException {
        Account account = null;
        try {
            account = restTemplate
                    .exchange(baseUrl + "accounts/" + id, HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
        } catch (RestClientResponseException ex) {
            throw new AccountServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
        return account;
        //
    }


    public double getAccountBalance() {
        double balance = 0;
        try {
            balance = restTemplate.exchange(baseUrl +"accounts/" + user1.getUser().getId() +"/balance", HttpMethod.GET, makeAuthEntity(), double.class).getBody();
            System.out.println("Your current account balance is: $" + balance);
        } catch (RestClientException e) {
            System.out.println("Error getting balance");
        }
        return balance;
    }


    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        return entity;
    }
    private HttpEntity makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }


}
