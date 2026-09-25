package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.AdoptionFormRequest;
import com.codecool.pawsandrequests.dto.AdoptionFormResponse;
import com.codecool.pawsandrequests.mapper.AdoptionFormMapper;
import com.codecool.pawsandrequests.model.AdoptionForm;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.AdoptionFormRepository;
import com.codecool.pawsandrequests.repository.PostRepository;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AdoptionFormService {

    private final AdoptionFormRepository adoptionFormRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AdoptionFormMapper adoptionFormMapper;

    public AdoptionFormService(
            final AdoptionFormRepository afr,
            final PostRepository pr,
            final UserRepository ur,
            final AdoptionFormMapper afm
    ) {
        this.adoptionFormRepository = afr;
        this.postRepository = pr;
        this.userRepository = ur;
        this.adoptionFormMapper = afm;
    }

    public final List<AdoptionFormResponse> getForms(
            final String email,
            final UUID postId
    ) {
        User user = userRepository.findByEmail(email).get();
        Post post = postRepository.findById(postId).get();

        if (user.getShelter() == null
                || !user.getShelter().getOrgNr().equals(
                post.getAnimal().getShelter().getOrgNr()
        )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can only access adoption forms from your own shelter");
        }
        return post.getAdoptionForms().stream()
                .map(adoptionFormMapper::toAdoptionFormResponse)
                .toList();
    }

    public final void createForm(
            final String email,
            final UUID postId,
            final AdoptionFormRequest request
    ) {
        User user = userRepository.findByEmail(email).get();
        Post post = postRepository.findById(postId).get();
        adoptionFormRepository.save(
                adoptionFormMapper.toAdoptionForm(request, user, post)
        );
    }

    public final List<AdoptionFormResponse> getAllForms(
            final String email
    ) {
        User user = userRepository.findByEmail(email).get();

        List<AdoptionForm> forms;

        switch (user.getRole()) {
            case ADMIN:
                forms = adoptionFormRepository.findAll();
                break;
            case SHELTERUSER:
                String orgNr = user.getShelter().getOrgNr();
                forms = adoptionFormRepository
                        .findByPostUserShelterOrgNr(orgNr);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Your role does not allow this");
        }

        return forms.stream()
                .map(adoptionFormMapper::toAdoptionFormResponse)
                .toList();
    }
}
