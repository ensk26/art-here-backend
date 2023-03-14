package com.backend.arthere.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t where t.member.id = :memberId")
    Optional<Token> findByMemberId(@Param("memberId") Long memberId);
    boolean existsByMemberId(Long memberId);
    boolean existsByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);
}