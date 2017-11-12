package com.ftudor.controller;

import com.ftudor.database.DogPictureRepository;
import com.ftudor.error.ApiError;
import com.ftudor.model.DogBreed;
import com.ftudor.model.DogPicture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * Unit tests for DogBreedService
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DogBreedControllerTest {
    private static final String URI_COLLECTION = "/dogbreed/picture";
    private static final String URI_PICTURE = "/dogbreed/picture/%s";
    private static final String URI_VOTEUP = "/dogbreed/picture/%s/up";
    private static final String URI_VOTEDOWN = "/dogbreed/picture/%s/down";
    private static final String URI_BREED = "/dogbreed/breed/%s";
    private static final String URI_PICTURE1 = "http://i.imgur.com/eE29vX4.png";
    private static final String URI_PICTURE2 = "http://i.imgur.com/xX2AeDR.png";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogPictureRepository mockRepository;

    private DogPicture picture1;
    private DogPicture picture2;
    private List<DogPicture> pictures;

    private ObjectMapper mapper;

    //set up before all tests
    @Before
    public void beforeClass(){
        picture1 = generatePicture(1, URI_PICTURE1, "Man with Dog", 10);
        picture2 = generatePicture(1, URI_PICTURE2, "Unhappy Dog", 5);
        pictures = new ArrayList<>();
        pictures.add(picture1);
        pictures.add(picture2);
        mapper = new ObjectMapper();
    }

    @After
    public void afterClass(){
        //clean up after all tests
        picture1 = null;
        pictures = null;
    }

    @Test
    public void testGetAllPictures() throws Exception{
        Mockito.when(mockRepository.findAllByOrderByVotesDesc()).thenReturn(pictures);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URI_COLLECTION).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        @SuppressWarnings("unchecked")
        List<DogPicture> dbPictures = mapper.readValue(response.getContentAsString(), List.class);
        assertNotNull(dbPictures);
        assertEquals(dbPictures.size(), pictures.size());
    }

    @Test
    public void testGetAllPicturesNoData() throws Exception {
        Mockito.when(mockRepository.findAll()).thenReturn(new ArrayList<DogPicture>());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(URI_COLLECTION).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    public void testGetBreedPicturesOk() throws Exception{
        Mockito.when(mockRepository.findByBreedOrderByVotesDesc(DogBreed.LABRADOR)).thenReturn(pictures);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(format(URI_BREED, DogBreed.LABRADOR.name())).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        @SuppressWarnings("unchecked")
        List<DogPicture> dbPictures = mapper.readValue(response.getContentAsString(), List.class);
        assertNotNull(dbPictures);
        assertEquals(dbPictures.size(), pictures.size());
    }

    @Test
    public void testGetBreedPicturesInvalidBreed() throws Exception{
        Mockito.when(mockRepository.findByBreedOrderByVotesDesc(null)).thenReturn(new ArrayList<DogPicture>());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(format(URI_BREED, "HUSKY")).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        ApiError error = mapper.readValue(response.getContentAsString(), ApiError.class);
        assertEquals(error.getCode(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testGetPictureDetailsOk() throws Exception {
        Mockito.when(mockRepository.findOne(1)).thenReturn(picture1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(format(URI_PICTURE, 1)).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        @SuppressWarnings("unchecked")
        DogPicture dbPicture = mapper.readValue(response.getContentAsString(), DogPicture.class);
        assertNotNull(dbPicture);
    }

    @Test
    public void testGetPictureDetailsNotFound() throws Exception {
        Mockito.when(mockRepository.findOne(100)).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(format(URI_PICTURE, 100)).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        ApiError error = mapper.readValue(response.getContentAsString(), ApiError.class);
        assertEquals(error.getCode(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testVoteUp() throws Exception{
        Mockito.when(mockRepository.findOne(1)).thenReturn(picture1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(format(URI_VOTEUP, 1)).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        int value = mapper.readValue(response.getContentAsString(), Integer.class);
        assertTrue(value > 0);
    }

    @Test
    public void testVodeDown() throws Exception{
        //use a different picture to avoid already voted error
        Mockito.when(mockRepository.findOne(2)).thenReturn(picture2);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(format(URI_VOTEDOWN, 2)).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        int value = mapper.readValue(response.getContentAsString(), Integer.class);
        assertTrue(value > 0);
    }

    //private methods

    private DogPicture generatePicture(int id, String url, String description, int votes){
        DogPicture picture = new DogPicture();
        picture.setId(id);
        picture.setUrl(url);
        picture.setDescription(description);
        picture.setVotes(new AtomicInteger(votes));
        picture.setBreed(DogBreed.LABRADOR);
        return picture;
    }
}
