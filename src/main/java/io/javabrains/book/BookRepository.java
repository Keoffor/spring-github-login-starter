package io.javabrains.book;

import io.javabrains.author.AuthorBooks;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends CassandraRepository<Book, String> {
    Optional<Book> findById(String authorid);
}
