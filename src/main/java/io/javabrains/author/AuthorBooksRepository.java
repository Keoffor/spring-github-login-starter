package io.javabrains.author;

import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;

public interface AuthorBooksRepository extends CassandraRepository<AuthorBooks, String> {


    Optional<AuthorBooks> findAllById(String authorid);
}
