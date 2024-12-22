package com.bookshelf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class BookshelfApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private BookshelfRepository bookRepo;

    @Test
    void shouldReturnABook() {
        ResponseEntity<String> response = restTemplate.getForEntity("/books/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        
        Number id = documentContext.read("$.id");
        String title = documentContext.read("$.title");
        Number bookReadNumber = documentContext.read("$.bookReadNumber");
        boolean bookRead = documentContext.read("$.bookRead");
        
        assertThat(id).isEqualTo(1);
        assertThat(title).isNotNull();
        assertThat(bookReadNumber).isEqualTo(0);
        assertThat(bookRead).isEqualTo(false);
        
    }
    
    @Test
    void shouldNotFoundABook() {
    	ResponseEntity<String> response = restTemplate.getForEntity("/books/99999", String.class);
    	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    @Test
    void shouldReturnAListOfBooks() {
    	ResponseEntity<String> response = restTemplate.getForEntity("/books/", String.class);
    	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    	
    	DocumentContext documentContext = JsonPath.parse(response.getBody());
    	Number lengthList = documentContext.read("$.length()");
    	assertTrue((int)lengthList > 0);
    }
    
    @Test
    void shouldNotReturnAListOfBooks() {
    	ResponseEntity<String> listOfBooks = restTemplate.getForEntity("/books/", String.class);
    	assertThat(listOfBooks.getStatusCode()).isEqualTo(HttpStatus.OK);
    	
    	DocumentContext documentContext = JsonPath.parse(listOfBooks.getBody());
    	Number listSize = documentContext.read("$.length()");
    	if((int)listSize > 0) {
        	assertTrue((int)listSize>0);
    	}else {
    		assertFalse((int)listSize==0);
    	}
    }
    
    @Test
    void shouldCreateANewBook() {
    	Book newBook = new Book(null, "TestTitle", "titleAuthor", 0, false);
        ResponseEntity<Void> response = restTemplate.postForEntity("/books/", newBook, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
    
    @Test
    void shouldUpdateABook() {
    	Book testBook = new Book(null, "testTitleJunit", "authorJunitTest", 0, false);
    	HttpEntity<Book> request = new HttpEntity<Book>(testBook);
    	
    	ResponseEntity<Void> response = restTemplate.exchange("/books/3", HttpMethod.PUT, request, Void.class);
    	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    	
    	ResponseEntity<String> responseTest = restTemplate.getForEntity("/books/3", String.class);
    	assertThat(responseTest.getStatusCode()).isEqualTo(HttpStatus.OK);
    	
    	
    	DocumentContext documentContext = JsonPath.parse(responseTest.getBody());
    	String titleTest = documentContext.read("$.title");
    	String authorTest = documentContext.read("$.author");
    	
    	assertThat(titleTest).isEqualTo("testTitleJunit");
    	assertThat(authorTest).isEqualTo("authorJunitTest");
    }
    
    /*
    @Test
    void shouldDeleteABook() {
    	Book newBook = new Book(null, "deleteTestTitle", "deleteAuthorTest", 0, false);
        ResponseEntity<Void> response = restTemplate.postForEntity("/books/", newBook, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String title = documentContext.read("$.title");
        
        		
        
    	ResponseEntity<Void> responseDelete = restTemplate.exchange("/books/4", HttpMethod.DELETE, null, Void.class);
    	assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    	
    }
    */

}
