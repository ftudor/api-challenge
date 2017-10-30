package com.ftudor.controller;

import com.ftudor.database.DogPictureRepository;
import com.ftudor.model.DogException;
import com.ftudor.model.DogBreed;
import com.ftudor.model.DogPicture;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * Dog Breed Controller
 */
@RestController
@RequestMapping("/dogbreed")
public class DogBreedController {
    private static final String EMPTY_COLLECTION = "Dog picture collection is empty";
    private static final String EMPTY_BREED = "Breed is empty";
    private static final String INVALID_BREED = "Breed is invalid";
    private static final String INVALID_URL = "Invalid image URL";
    private static final String PICTURE_NOT_FOUND = "Picture not found";
    private static final String ALREADY_VOTED = "Already voted";

    private static final Logger LOGGER = LoggerFactory.getLogger(DogBreedController.class);

    //keep voted info - concurrent access possible
    private static final Set<String> VOTED = new ConcurrentSkipListSet<>();

    private DogPictureRepository repository;

    @Autowired
    public void setRepository(final DogPictureRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/picture")
    public List<DogPicture> getAllPictures() {
        LOGGER.info("In getAllPictures");
        //read all pictures from DB
        Iterator<DogPicture> it = repository.findAllByOrderByVotesDesc().iterator();
        List<DogPicture> dbPictures = new ArrayList<>();
        while(it.hasNext()){
            dbPictures.add(it.next());
        }

        //return 204 No Content if collection is empty
        if(dbPictures.isEmpty()) {
            throw new DogException(HttpStatus.NO_CONTENT.value(), "No content");
        }

        return dbPictures;
    }

    @GetMapping("/breed/{breed}")
    public List<DogPicture> getBreedPictures(@PathVariable final String breed) {
        LOGGER.info("In getBreedPictures");
        //return 400 if breed is not specified
        if(StringUtils.isBlank(breed)){
            throw new DogException(HttpStatus.BAD_REQUEST.value(), EMPTY_BREED);
        }

        //read from DB
        List<DogPicture> dbPictures = new ArrayList<>();
        DogBreed dogBreed;

        //check if passed some unsupported breed
        try{
            dogBreed = DogBreed.valueOf(breed);
        }
        catch (IllegalArgumentException e){
            LOGGER.error("Caught unsupported breed {}", breed);
            //throw new IllegalArgumentException(INVALID_BREED);
            throw new DogException(HttpStatus.BAD_REQUEST.value(), INVALID_BREED);

        }
        dbPictures = repository.findByBreedOrderByVotesDesc(dogBreed);


        //return 204 No Content if collection is empty
        if(dbPictures.isEmpty()){
            throw new IllegalStateException(EMPTY_COLLECTION);
        }

        return dbPictures;
    }

    @GetMapping("/picture/{pictureId}")
    public DogPicture getPictureDetails(@PathVariable final int pictureId) {
        LOGGER.info("In getPictureDetails");
        //check if ID is valid, return 400 if not
        if(pictureId <= 0){
            throw new DogException(HttpStatus.BAD_REQUEST.value(), "Invalid URL");
        }

        //read from DB
        DogPicture dbPicture = repository.findOne(pictureId);
        if(dbPicture == null){
            throw new DogException(HttpStatus.NOT_FOUND.value(), PICTURE_NOT_FOUND);
        }

        return dbPicture;
    }

    @GetMapping("/picture/{pictureId}/up")
    public int voteUp(@PathVariable final int pictureId, HttpServletRequest request) {
        LOGGER.info("In voteUp");
        DogPicture dbPicture = getPicture(pictureId, request.getRemoteAddr());

        //store vote
        dbPicture.getVotes().incrementAndGet();
        repository.save(dbPicture);

        //return number of up votes
        return dbPicture.getVotes().intValue();

    }

    @GetMapping("/picture/{pictureId}/down")
    public int voteDown(@PathVariable final int pictureId, HttpServletRequest request) {
        LOGGER.info("In voteDown");
        DogPicture dbPicture = getPicture(pictureId, request.getRemoteAddr());

        //store vote
        dbPicture.getVotes().decrementAndGet();
        repository.save(dbPicture);

        //return number of down votes
        return dbPicture.getVotes().intValue();
    }

    //common checks for vote
    private DogPicture getPicture(int pictureId, String remoteAddr){
        //check if ID is valid, return 400 if not
        if(pictureId <= 0){
            throw new DogException(HttpStatus.BAD_REQUEST.value(), INVALID_URL);
        }

        //read from DB
        DogPicture dbPicture = repository.findOne(pictureId);
        if(dbPicture == null){
            throw new DogException(HttpStatus.NOT_FOUND.value(), PICTURE_NOT_FOUND);
        }

        if(VOTED.contains(getKey(remoteAddr, pictureId))){
            //if already voted return HTTP code 401
            throw new DogException(HttpStatus.UNAUTHORIZED.value(), ALREADY_VOTED);
        }
        else{
            VOTED.add(getKey(remoteAddr, pictureId));
        }

        return dbPicture;
    }

    private String getKey(String remoteAddr, int pictureId){
        return remoteAddr + "-" + pictureId;

    }
}
