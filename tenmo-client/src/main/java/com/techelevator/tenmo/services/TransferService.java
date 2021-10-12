package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator
        .tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Scanner;
import java.util.logging.Logger;

public class TransferService {
    public static String AUTH_TOKEN = "";
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user1;
    private Account account1;

    private final Logger LOGGER = Logger.getLogger(TransferService.class.getCanonicalName());

    public TransferService(String url, AuthenticatedUser user1, Account account1) {
        String uri = url + "accounts/user/full/" + user1.getUser().getId();
        LOGGER.info("VISITING THIS URL " + uri);
        if (account1 == null) {
            this.account1 = restTemplate.exchange(uri,
                     HttpMethod.GET, makeAuthEntity(), Account.class).getBody();

        }
        this.user1= user1;
        baseUrl = url;

    }

    //Get the user account

    public Transfer getTransferByTransferId(int transferId) throws TransferServiceException {
        Transfer transfer = null;

        try {
            transfer = restTemplate.exchange(baseUrl + "users/" + user1.getUser().getId() + "/transfers/"
                    + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();

        } catch (RestClientResponseException e) {
            throw new TransferServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
        return transfer;
    }

    public Transfer getTransferByTransferType(int transferId) throws TransferServiceException {
        Transfer transfer = null;

        try {
            transfer = restTemplate.exchange(baseUrl + "users/" + user1.getUser().getId() + "/transfers/"
                    + transferId + "/transfer_type/", HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
        } catch (RestClientResponseException e) {
            throw new TransferServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }
        return transfer;
    }

    public Transfer[] getTransferHistory(double accountId, int accountType) throws TransferServiceException {
        Transfer[] transfers = null;

        try {
            transfers = restTemplate.exchange(baseUrl + "transfers/users/" +(int)accountId + "/" + accountType,
                    HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            LOGGER.info("The transfers are listed " + transfers.length + " Transfer 1 is " + transfers[0]);

        } catch (RestClientResponseException e) {
            throw new TransferServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
        }

        return transfers;
    }



    public Transfer addTransferRequest(AuthenticatedUser user1) throws TransferServiceException {
        Transfer transfer = null;

        try {
            transfer = restTemplate.postForObject(baseUrl + "users/" + transfer.getAccount_from() + "/transfers/request",
                    makeTransferEntity(transfer), Transfer.class);
        } catch (RestClientResponseException ex) {
            throw new TransferServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
        return transfer;
    }

    public Transfer addTransfer(AuthenticatedUser user1) throws TransferServiceException {
        Transfer transfer = null;

        try {
            transfer = restTemplate.postForObject(baseUrl + "users/" + transfer.getAccount_to() + "/transfers/send",
                    makeTransferEntity(transfer), Transfer.class);
        } catch (RestClientResponseException ex) {
            throw new TransferServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
        return transfer;
    }

    public Transfer update(AuthenticatedUser user1) throws TransferServiceException {
        Transfer transfer = null;
        try {
            restTemplate.put(baseUrl + "users/" + user1.getUser().getId() + "/balance/withdraw", makeTransferEntity(transfer));
        } catch (RestClientResponseException ex) {
            throw new TransferServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        }
        return transfer;
    }

    public void transfersList(int accountType) {

        Transfer [] output = null;

        try {
            output = getTransferHistory(this.account1.getAccount_Id(), accountType);
            System.out.println("-------------------------------------------\r\n" +

                    "Transfers\r\n" +

                    "ID          From/To                 Amount\r\n" +

                    "-------------------------------------------\r\n");

            String fromOrTo = "";

            String name = "";

            for (Transfer i : output) {

                if (user1.getUser().getId() == i.getAccount_from()) {

                    fromOrTo = "From: ";

                    name = String.valueOf((int)i.getAccount_to());

                } else {

                    fromOrTo = "To: ";
                    name = String.valueOf((int)i.getAccount_from());

                }
                System.out.println(i.getTransfer_Id() +"\t\t" + fromOrTo + name + "\t\t$" + i.getAmount());
            }

            System.out.print("-------------------------------------------\r\n" +

                    "Please enter transfer ID to view details (0 to cancel): ");

            Scanner scanner = new Scanner(System.in);

            String input = scanner.nextLine();

            if (Integer.parseInt(input) != 0) {

                boolean foundTransferId = false;

                for (Transfer i : output) {

                    if (Integer.parseInt(input) == i.getTransfer_Id()) {

                        Transfer temp = restTemplate.exchange(baseUrl + "transfers/" + i.getTransfer_Id(), HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();

                        foundTransferId = true;

                        System.out.println("--------------------------------------------\r\n" +

                                "Transfer Details\r\n" +

                                "--------------------------------------------\r\n" +

                                " Id: "+ temp.getTransfer_Id() + "\r\n" +

                                " From: " + temp.getAccount_from() + "\r\n" +

                                " To: " + temp.getAccount_to() + "\r\n" +

                                " Type: " + temp.getTransfer_Type_desc() + "\r\n" +

                                " Status: " + temp.getTransfer_Status_desc() + "\r\n" +

                                " Amount: $" + temp.getAmount());
                    }

                }

                if (!foundTransferId) {

                    System.out.println("Not a valid transfer ID");

                }

            }

        } catch (Exception | TransferServiceException e) {

            System.out.println("Something went wrong... Opps! We have all your money now!");

        }



    }

    public void transactionForBucks
            (String message, int accountType) {
        User[] users = null;

        try {
            Scanner scanner = new Scanner(System.in);
            LOGGER.info("CURRENT USER INFO " + user1.getUser().toString());
            users = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println("-------------------------------------------\r\n" +
                    "Users\r\n" +
                    "ID\t\tName\r\n" +
                    "-------------------------------------------");
            Arrays.stream(users)
                    .filter(x -> !x.getUsername().equalsIgnoreCase(user1.getUser().getUsername()))
                    .forEach(x -> System.out.println(x.getId() + "\t\t" + x.getUsername()));

            System.out.print(String.format("-------------------------------------------\r\n" +
                    "Enter ID of user you are %s message (0 to cancel):", message));

            // get accounts by user ids /accounts/user/{id}/

            long account_to = restTemplate.exchange(baseUrl + String.format("accounts/user/%d/",Integer.parseInt(scanner.nextLine()) ), HttpMethod.GET, makeAuthEntity(), Long.class).getBody();
            long account_from = restTemplate.exchange(baseUrl + String.format("accounts/user/%d/", user1.getUser().getId()), HttpMethod.GET, makeAuthEntity(), Long.class).getBody();
            Transfer transfer = new Transfer();
            transfer.setAccount_to(account_to);
            transfer.setAccount_from(account_from);
            transfer.setTransfer_Type_Id(accountType);


            if (transfer.getAccount_to() != 0) {
                System.out.print(String.format("Enter amount %s:", message));
                try {
                    transfer.setAmount(scanner.nextDouble());
                } catch (NumberFormatException e) {
                    System.out.println("Error when entering amount");
                }
                String output = restTemplate.exchange(baseUrl + "transfers", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
                System.out.println(output);
                LOGGER.info("Transfer was completed");
            }
        } catch (Exception e) {
            Logger.getLogger("TransferService.java").info(e.getLocalizedMessage());
            //System.out.println("Bad input.");
        }
    }

    public void requestForBucks() {
        List<User> users = new ArrayList<>();
        Transfer transfer1 = new Transfer();
        try {
            Scanner scanner = new Scanner(System.in);
            users = restTemplate.exchange(baseUrl + "transfer/transfer_type/transfer_type_desc", HttpMethod.GET, makeAuthEntity(), List.class).getBody();
            System.out.println("-------------------------------------------\r\n" +
                    "Users\r\n" +
                    "ID\t\tName\r\n" +
                    "-------------------------------------------");
            for (User user2 : users) {
                if (user2.getId() != user1.getUser().getId()) {
                    System.out.println(user2.getId() + "\t\t" + user2.getUsername());
                }
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Enter ID of user you are requesting from (0 to cancel): ");
            transfer1.setAccount_to((long)user1.getUser().getId());
            transfer1.setAccount_from(Long.parseLong(scanner.nextLine()));
            if (transfer1.getAccount_to() != 0) {
                System.out.print("Enter amount: ");
                try {
                    transfer1.setAmount(scanner.nextDouble());
                } catch (NumberFormatException e) {
                    System.out.println("Error when entering amount");
                }
                String output = restTemplate.exchange(baseUrl + "request", HttpMethod.POST, makeTransferEntity(transfer1), String.class).getBody();
                System.out.println(output);
            }
        } catch (Exception e) {
            System.out.println("Bad input.");
        }
    }


    /*
    public List<Transfer> transferListToRejectOrApprove() {
        List<Transfer> transfer = new ArrayList<>();
        try {
            transfer = restTemplate.exchange(baseUrl + "account/transfers/" + user1.getUser().getId(), HttpMethod.GET, makeAuthEntity(), List.class).getBody();
            System.out.println("-------------------------------------------\r\n" +
                    "Transfers\r\n" +
                    "ID          From/To                 Amount\r\n" +
                    "-------------------------------------------\r\n");
            String fromOrTo = "";
            String name = "";
            for (Transfer transfer1 : transfer) {
                if (user1.getUser().getId() == transfer1.getAccount_from()) {
                    fromOrTo = "From: ";
                    name = transfer1.getUser_From();
                } else {
                    fromOrTo = "To: ";
                    name = transfer1.getUser_To();
                }
                System.out.println(transfer1.getTransfer_Id() +"\t\t" + fromOrTo + name + "\t\t$" + transfer1.getAmount());
            }
            System.out.print("-------------------------------------------\r\n" +
                    "Please enter transfer ID to view details (0 to cancel): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (Integer.parseInt(input) != 0) {
                boolean foundTransferId = false;

            for (Transfer transfer3 : transfer) {
                if (transfer3.getAccount_to() != user1.getUser().getId()) {
                    if (Integer.parseInt(input) == transfer3.getTransfer_Id()) {
                        System.out.print("-------------------------------------------\r\n" +
                                transfer3.getTransfer_Id() +"\t\t" + fromOrTo + name + "\t\t$" + transfer3.getAmount() + "\r\n" +
                                "1: Approve\r\n" +
                                "2: Reject\r\n" +
                                "0: Don't approve or reject\r\n" +
                                "--------------------------\r\n" +
                                "Please choose an option: ");
                        try {
                            int id = 1 + Integer.parseInt(scanner.nextLine());
                            if (id != 1) {
                                String results = restTemplate.exchange(baseUrl + "accounts/transfers" + id, HttpMethod.PUT, makeTransferEntity(transfer3), String.class).getBody();
                                System.out.println(results);
                                foundTransferId = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Not a valid transfer option");
                        }
                        if (!foundTransferId) {
                            System.out.println("Not a valid transfer ID");
                        }
                    }
                } else {
                    System.out.println("You can not approve/reject your own request.");
                }
            }
        }
    } catch (Exception e) {
        System.out.println("Something went wrong. Please try again");
    }
		return transfer;
}

     */

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}