package io.javabrains.book;


import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(value="book_by_id")
public class Book {
    @Id @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;
    @Column("book_name")
    @CassandraType(type= CassandraType.Name.TEXT)
    private String name;

    @Column("description")
    @CassandraType(type= CassandraType.Name.TEXT)
    private String description;

    @Column("published_date")
    @CassandraType(type= CassandraType.Name.DATE)
    private LocalDate publishedDate;

    @Column("cover_ids")
    @CassandraType(type= CassandraType.Name.LIST, typeArguments = CassandraType.Name.INT)
    private List<Integer> coverIds;

    @Column("author_names")
    @CassandraType(type= CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorNames;

    @Column("author_id")
    @CassandraType(type= CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorIds;


    public Book() {
    }
    public List<String> convertCoverIds(List<Integer> coverids){
        List<String>converter = new ArrayList<>();
        for(int coverid: coverids){
            converter.add(String.valueOf(coverid));
        }
        return converter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<Integer> getCoverIds() {
        return coverIds;
    }

    public void setCoverIds(List<Integer> coverIds) {
        this.coverIds = coverIds;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public List<String> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<String> authorIds) {
        this.authorIds = authorIds;
    }
}
