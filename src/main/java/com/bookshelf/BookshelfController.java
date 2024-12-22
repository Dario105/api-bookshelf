package com.bookshelf;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/books")
public class BookshelfController {

	@Autowired
	private BookshelfRepository bookRepo;

	@GetMapping("/{id}")
	public ResponseEntity<Book> findById(@PathVariable(name = "id") Long id) {
	    return bookRepo.findById(id)
	                   .map(ResponseEntity::ok) 
	                   .orElse(ResponseEntity.notFound().build()); // Si no existe, devuelve 404 Not Found.
	}

	@GetMapping("/")
	private ResponseEntity<Iterable<Book>> findAll() {
		Iterable<Book> listBooks = bookRepo.findAll();
		return ResponseEntity.ok(listBooks);
	}
	
	@GetMapping("/author/{author}")
	private ResponseEntity<List<Book>> findByAuthor(@PathVariable(name = "author") String author){
		List<Book> listBookAuthor = bookRepo.findByAuthor(author);
		return ResponseEntity.ok(listBookAuthor);
	}
	
	@GetMapping("/title/{title}")
	private ResponseEntity<Book> findByTitle(@PathVariable(name = "title") String title){
		Book listBookAuthor = bookRepo.findByTitle(title);
		return ResponseEntity.ok(listBookAuthor);
	}

	@PostMapping("/")
	private ResponseEntity<Void> createNewBook(@RequestBody Book recibedBook, UriComponentsBuilder ucb) {
	    Book newBook = new Book(null, recibedBook.getTitle(), recibedBook.getAuthor(), recibedBook.getBookReadNumber(), recibedBook.isBookRead());
	    Book myBook = bookRepo.save(newBook);
	    URI locationOfNewBook = ucb.path("books/{id}").buildAndExpand(myBook.getId()).toUri();
	    return ResponseEntity.created(locationOfNewBook).build();
	}
	
	@PutMapping("/{id}")
	private ResponseEntity<Void> updateBook(@PathVariable(name = "id") Long id,@RequestBody Book updatedBook) {
		if(!bookRepo.findById(id).isEmpty()) {
			Book toUpdateBook = bookRepo.findById(id).get();
			toUpdateBook.setId(id);
			toUpdateBook.setTitle(updatedBook.getTitle());
			toUpdateBook.setAuthor(updatedBook.getAuthor());
			toUpdateBook.setBookReadNumber(updatedBook.getBookReadNumber());
			toUpdateBook.setBookRead(updatedBook.isBookRead());
			
			bookRepo.save(toUpdateBook);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	private ResponseEntity<Void> deleteBook(@PathVariable(name="id") Long id) {
		if (bookRepo.existsById(id)) {
			bookRepo.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	

}
