package com.ftudor.database;

import com.ftudor.model.DogBreed;
import com.ftudor.model.DogPicture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  JPA repository for DogPicture
 */
@Repository
public interface DogPictureRepository extends CrudRepository<DogPicture, Integer> {
    //find by breed, order by votes desc
    List<DogPicture> findByBreedOrderByVotesDesc(@Param(value = "breed") DogBreed breed);

    //find all, order by votes desc
    List<DogPicture> findAllByOrderByVotesDesc();
}
