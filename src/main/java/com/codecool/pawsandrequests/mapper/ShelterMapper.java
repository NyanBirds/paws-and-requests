package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.dto.ShelterResponse;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Shelter;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShelterMapper {
    ShelterResponse toShelterInfo(Shelter shelter);
}
