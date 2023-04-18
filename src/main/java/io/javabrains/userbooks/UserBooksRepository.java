package io.javabrains.userbooks;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface UserBooksRepository extends CassandraRepository <UserBooks, UserBooksPrimaryKey>{

}
