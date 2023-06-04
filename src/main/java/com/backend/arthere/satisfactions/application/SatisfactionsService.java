package com.backend.arthere.satisfactions.application;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.exception.MemberNotFoundException;
import com.backend.arthere.satisfactions.domain.SatisfactionType;
import com.backend.arthere.satisfactions.domain.Satisfactions;
import com.backend.arthere.satisfactions.domain.SatisfactionsRepository;
import com.backend.arthere.satisfactions.dto.request.AddSatisfactionsRequest;
import com.backend.arthere.satisfactions.dto.request.CreateSatisfactionsRequest;
import com.backend.arthere.satisfactions.dto.response.GetTotalToDetailsResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsCountResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsListResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsResponse;
import com.backend.arthere.starRatings.domain.StarRatings;
import com.backend.arthere.starRatings.domain.StarRatingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SatisfactionsService {

    private final SatisfactionsRepository satisfactionsRepository;

    private final StarRatingsRepository starRatingsRepository;

    private final ArtsRepository artsRepository;

    private final MemberRepository memberRepository;

    public SatisfactionsListResponse findSatisfactionsList(Long id) {

        List<SatisfactionsCountResponse> satisfactionsCount = satisfactionsRepository.findSatisfactionsCount(id);
        if (satisfactionsCount.isEmpty()) {
            return new SatisfactionsListResponse();
        }
        GetTotalToDetailsResponse total = satisfactionsRepository.getTotalToDetailsById(id);

        return new SatisfactionsListResponse(satisfactionsCount, total.getStarRating(), total.getPostCount());
    }

    public SatisfactionsResponse findSatisfactions(Long artId, Long userId) {

        List<SatisfactionType> satisfactionsType = satisfactionsRepository.findSatisfactionsType(artId, userId);

        List<String> satisfactions = new ArrayList<>();
        for (SatisfactionType type : satisfactionsType) {
            satisfactions.add(type.getSatisfactionName());
        }
        Integer starRatings = starRatingsRepository.findStarRatingsId(artId, userId);

        return new SatisfactionsResponse(satisfactions, starRatings);
    }


    public void createSatisfactions(CreateSatisfactionsRequest request, Long userId) throws Exception {

        Member member;
        Arts arts;
        try {
            arts = artsRepository.getReferenceById(request.getArtsId());
        } catch (Exception e) {
            throw new ArtsNotFoundException();
        }

        try {
            member = memberRepository.getReferenceById(userId);
        } catch (EntityNotFoundException e) {
            throw new MemberNotFoundException();
        }

        List<Satisfactions> satisfactions = new ArrayList<>();

        for (SatisfactionType type : request.getSatisfactionsType()) {
            satisfactions.add(new Satisfactions(arts, member, type));
        }
        satisfactionsRepository.saveAll(satisfactions);
        starRatingsRepository.save(new StarRatings(arts, member, request.getStarRating()));
        arts.updateStarRating(arts.getStarRating() + request.getStarRating());
        arts.updateStarRatingCount(arts.getStarRatingCount() + 1);
        artsRepository.save(arts);
    }

    public void addSatisfactions(AddSatisfactionsRequest request, Long userId) throws Exception {

        Member member;
        Arts arts;
        try {
            arts = artsRepository.getReferenceById(request.getArtsId());
            System.out.println("arts = " + arts);
        } catch (EntityNotFoundException e) {
            throw new ArtsNotFoundException();
        }

        try {
            member = memberRepository.getReferenceById(userId);
        } catch (EntityNotFoundException e) {
            throw new MemberNotFoundException();
        }

        if (!request.getDeleteSatisfactions().isEmpty()) {

            satisfactionsRepository.deleteSatisfactions(request.getArtsId(), userId, request.getDeleteSatisfactionsType());
        }
        if (!request.getAddSatisfactions().isEmpty()) {

            List<Satisfactions> satisfactions = new ArrayList<>();

            for (SatisfactionType type : request.getAddSatisfactionsType()) {
                satisfactions.add(new Satisfactions(arts, member, type));
            }
            satisfactionsRepository.saveAll(satisfactions);
        }
        if (request.getStarRating() != null) {

            StarRatings starRatings = starRatingsRepository.findStarRatings(request.getArtsId(), userId);
            arts.updateStarRating(arts.getStarRating() - starRatings.getStarRating() + request.getStarRating());
            artsRepository.save(arts);

            starRatings.updateStarRating(request.getStarRating());
            starRatingsRepository.save(starRatings);
        }
    }
}
