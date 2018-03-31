package ru.sbt.jschool.session3.problem1;

import java.util.*;

/**
 */
public class AccountServiceImpl implements AccountService {
    protected FraudMonitoring fraudMonitoring;
    Map<Long,ArrayList<Account>> mapClients = new TreeMap<>();
    Map<Long,Account> mapAccount = new TreeMap<>();
    Set<Long> payments = new HashSet<>();

    public AccountServiceImpl(FraudMonitoring fraudMonitoring) {
        this.fraudMonitoring = fraudMonitoring;
    }

    @Override public Result create(long clientID, long accountID, float initialBalance, Currency currency) {
        if(fraudMonitoring.check(clientID)){
            return Result.FRAUD;
        }
        Account account = new Account(clientID,accountID,currency,initialBalance);
        mapAccount.put(accountID,account);
        ArrayList<Account> listAccount = mapClients.get(clientID);
        if(listAccount == null){
            listAccount = new ArrayList<>();
        }
        else{
            for (Account ac : listAccount){
                if(ac.getAccountID()==accountID){
                    return Result.ALREADY_EXISTS;
                }
            }
        }
        listAccount.add(account);
        mapClients.put(clientID,listAccount);
        return Result.OK;
    }

    @Override public List<Account> findForClient(long clientID) {
        return mapClients.get(clientID);
    }

    @Override public Account find(long accountID) {
        return mapAccount.get(accountID);
    }

    @Override public Result doPayment(Payment payment) {
        if(payments.contains(payment.getOperationID())){
            return Result.ALREADY_EXISTS;
        }
        payments.add(payment.getOperationID());

        List<Account> accountsPayer = findForClient(payment.getPayerID());
        if(accountsPayer == null) return Result.PAYER_NOT_FOUND;
        Account accPayer = null;
        for (Account acc : accountsPayer){
            if(acc.getAccountID()==payment.getPayerAccountID()){
                accPayer = acc;
            }
        }
        if(accPayer==null){
            return Result.PAYER_NOT_FOUND;
        }
        List<Account> accountsRecipient = findForClient(payment.getRecipientID());
        if(accountsRecipient==null) return Result.RECIPIENT_NOT_FOUND;
        Account accRecipient = null;
        for (Account acc : accountsRecipient){
            if(acc.getAccountID()==payment.getRecipientAccountID()){
                accRecipient = acc;
            }
        }
        if(accRecipient==null){
            return Result.RECIPIENT_NOT_FOUND;
        }
        if(fraudMonitoring.check(payment.getPayerID())||fraudMonitoring.check(payment.getRecipientID())){
            return Result.FRAUD;
        }

        if(accPayer.getBalance()<payment.getAmount()){
            return Result.INSUFFICIENT_FUNDS;
        }

        Currency currencyPayer = accPayer.getCurrency();
        Currency currencyRecipient = accRecipient.getCurrency();
        accPayer.setBalance(accPayer.getBalance()-payment.getAmount());
        if(!currencyPayer.equals(currencyRecipient)){
        accRecipient.setBalance(accRecipient.getBalance()+currencyPayer.to(payment.getAmount(),currencyRecipient));}
        else {
            accRecipient.setBalance(accRecipient.getBalance()+payment.getAmount());
        }

        return Result.OK;
    }
}
