package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.AnimalRequest;
import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Shelter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalMapper {
    AnimalResponse toAnimalResponse(Animal animal);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shelter", source = "shelter")
    Animal toAnimal(AnimalRequest animalRequest, Shelter shelter);
}
