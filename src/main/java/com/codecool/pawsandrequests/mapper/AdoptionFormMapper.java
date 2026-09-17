package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.AdoptionFormRequest;
import com.codecool.pawsandrequests.dto.AdoptionFormResponse;
import com.codecool.pawsandrequests.model.AdoptionForm;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdoptionFormMapper {
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "animalId", source = "post.animal.id")
    AdoptionFormResponse toAdoptionFormResponse(AdoptionForm adoptionForm);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    AdoptionForm toAdoptionForm(
            AdoptionFormRequest request,
            User user,
            Post post
    );
}
