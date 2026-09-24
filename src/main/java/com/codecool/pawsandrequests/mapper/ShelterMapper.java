package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.dto.ShelterResponse;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Shelter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ShelterMapper {
    @Mapping(target = "url", source = "shelter", qualifiedByName = "pictureUrl")
    ShelterResponse toShelterInfo(Shelter shelter);

    @Named("pictureUrl")
    default Optional<String> pictureUrl(final Shelter shelter) {
        return Optional.ofNullable(shelter.getPicture())
                .map(picture -> "/pictures/" + picture.getId());
    }
}
