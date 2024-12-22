package com.bookshelf;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookshelfRepository extends CrudRepository<Book, Long> {
	Book findByTitle(String title);
	List<Book> findByAuthor(String author);
}
