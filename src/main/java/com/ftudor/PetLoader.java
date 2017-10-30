package com.ftudor;

import com.ftudor.database.DogPictureRepository;
import com.ftudor.model.DogBreed;
import com.ftudor.model.DogPicture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads stored objects from the file system and builds up
 * the appropriate objects to add to the data source.
 */
@Component
public class PetLoader implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(PetLoader.class);

    // Resources to the different files we need to load.
    @Value("classpath:data/labrador.txt")
    private Resource labradors;

    @Value("classpath:data/pug.txt")
    private Resource pugs;

    @Value("classpath:data/retriever.txt")
    private Resource retrievers;

    @Value("classpath:data/yorkie.txt")
    private Resource yorkies;

    private DogPictureRepository pictureRepository;

    @Autowired
    public void setPictureRepository(final DogPictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    /**
     * Load the different breeds into the data source after
     * the application is ready.
     *
     * @throws Exception In case something goes wrong while we load the breeds.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        loadBreed(DogBreed.LABRADOR, labradors);
        loadBreed(DogBreed.PUG, pugs);
        loadBreed(DogBreed.RETRIEVER, retrievers);
        loadBreed(DogBreed.YORKIE, yorkies);
    }

    /**
     * Reads the list of dogs in a category and (eventually) add
     * them to the data source.
     * @param breed The breed that we are loading.
     * @param source The file holding the breeds.
     * @throws IOException In case things go horribly, horribly wrong.
     */
    private void loadBreed(DogBreed breed, Resource source) throws IOException {
        try ( BufferedReader br = new BufferedReader(new InputStreamReader(source.getInputStream()))) {
            String line;
            //pictures to be saved to DB
            List<DogPicture> pictures = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                //create appropriate objects and save them to the datasource.
                DogPicture picture = new DogPicture();
                //todo we could validate the URLs using e.g Apache Commons UrlValidator
                picture.setUrl(line);
                picture.setBreed(breed);
                //set ID for a new entry; will be auto-generated
                picture.setId(-1);
                //load info about the image like dpi, resolution
                loadImageInfo(picture);
                pictures.add(picture);
            }
            //save to DB
            pictureRepository.save(pictures);
            LOGGER.info("Loaded data from file {}", source.getFilename());

        }
    }

    private void loadImageInfo(DogPicture picture) {
        //todo use using org.apache.http.client.HttpClient to make a get call and load image information
        //todo call entity.getContent() and update DogPicture object
    }
}
