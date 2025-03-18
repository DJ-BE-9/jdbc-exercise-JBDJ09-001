package com.nhnacademy.jdbc.bank.service.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.exception.AccountAreadyExistException;
import com.nhnacademy.jdbc.bank.exception.AccountNotFoundException;
import com.nhnacademy.jdbc.bank.exception.BalanceNotEnoughException;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;
import com.nhnacademy.jdbc.bank.repository.impl.AccountRepositoryImpl;
import com.nhnacademy.jdbc.bank.service.BankService;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.LambdaConversionException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository;

    public BankServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccount(Connection connection, long accountNumber){
        //todo#11 계좌-조회
/*        if(Objects.isNull(connection) || Objects.isNull(accountNumber)){
            throw new IllegalArgumentException();
        }*/

        if(this.accountRepository.findByAccountNumber(connection, accountNumber).isEmpty()){
            return null;
        }

        return this.accountRepository.findByAccountNumber(connection, accountNumber).get();
    }

    @Override
    public void createAccount(Connection connection, Account account){
        //todo#12 계좌-등록
/*        if(Objects.isNull(connection) || Objects.isNull(account)){
            throw new IllegalArgumentException();
        }*/
        if(!isExistAccount(connection,account.getAccountNumber())){
            this.accountRepository.save(connection, account);
        }else{
            throw new AccountAreadyExistException(account.getAccountNumber());
        }

    }

    @Override
    public boolean depositAccount(Connection connection, long accountNumber, long amount){
        //todo#13 예금, 계좌가 존재하는지 체크 -> 예금실행 -> 성공 true, 실패 false;
/*        if(Objects.isNull(connection) || Objects.isNull(accountNumber) || Objects.isNull(amount) ){
            throw new IllegalArgumentException();
        }*/
        if(!isExistAccount(connection,accountNumber)){
            throw new AccountNotFoundException(accountNumber);
        }else{
            if(this.accountRepository.deposit(connection, accountNumber, amount) == 1){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean withdrawAccount(Connection connection, long accountNumber, long amount){
        //todo#14 출금, 계좌가 존재하는지 체크 ->  출금가능여부 체크 -> 출금실행, 성공 true, 실폐 false 반환
/*        if(Objects.isNull(connection) || Objects.isNull(accountNumber) || Objects.isNull(amount) ){
            throw new IllegalArgumentException();
        }*/
        if(!isExistAccount(connection,accountNumber)){
            throw new AccountNotFoundException(accountNumber);
        }else{
            Optional<Account> account = this.accountRepository.findByAccountNumber(connection, accountNumber);
            if(!account.isEmpty()){
                if(account.get().getBalance() < amount){
                    throw new BalanceNotEnoughException(accountNumber);
                }
                if(this.accountRepository.withdraw(connection,accountNumber,amount)==1){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void transferAmount(Connection connection, long accountNumberFrom, long accountNumberTo, long amount){
        //todo#15 계좌 이체 accountNumberFrom -> accountNumberTo 으로 amount만큼 이체
/*        if(Objects.isNull(connection) || Objects.isNull(accountNumberFrom) || Objects.isNull(accountNumberTo) || Objects.isNull(amount) ){
            throw new IllegalArgumentException();
        }*/
        if(!isExistAccount(connection,accountNumberFrom)){
            throw new AccountNotFoundException(accountNumberFrom);
        }
        Optional<Account> account1 = this.accountRepository.findByAccountNumber(connection, accountNumberFrom);
        if(account1.isEmpty()){
            throw new AccountNotFoundException(accountNumberFrom);
        }
        if(!isExistAccount(connection,accountNumberTo)){
            throw new AccountNotFoundException(accountNumberTo);
        }
        Optional<Account> account2 = this.accountRepository.findByAccountNumber(connection, accountNumberFrom);
        if(account2.isEmpty()){
            throw new AccountNotFoundException(accountNumberFrom);
        }
        if(account1.get().getBalance() < amount){
            throw new BalanceNotEnoughException(accountNumberFrom);
        }
        if(this.accountRepository.withdraw(connection,accountNumberFrom,amount)!=1){
            throw new RuntimeException();
        }
        if(this.accountRepository.deposit(connection, accountNumberTo, amount) != 1){
            throw new RuntimeException();
        }
    }

    @Override
    public boolean isExistAccount(Connection connection, long accountNumber){
        //todo#16 Account가 존재하면 true , 존재하지 않다면 false
/*        if(Objects.isNull(connection) || Objects.isNull(accountNumber)){
            throw new IllegalArgumentException();
        }*/
        if(this.accountRepository.countByAccountNumber(connection,accountNumber)==1){
            return true;
        }
        return false;
    }

    @Override
    public void dropAccount(Connection connection, long accountNumber) {
        //todo#17 account 삭제
/*        if(Objects.isNull(connection) || Objects.isNull(accountNumber)){
            throw new IllegalArgumentException();
        }*/
        if(isExistAccount(connection,accountNumber)){
            if(this.accountRepository.deleteByAccountNumber(connection,accountNumber)!=1){
                throw new RuntimeException();
            }
        }else{
            throw new AccountNotFoundException(accountNumber);
        }
    }

}