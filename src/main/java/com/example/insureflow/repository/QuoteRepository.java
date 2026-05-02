package com.example.insureflow.repository;

import com.example.insureflow.model.Quote;
import com.example.insureflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    List<Quote> findByUser(User user);
}