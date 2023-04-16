package io.javabrains.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Controller
public class BookController {
    @Autowired
    BookRepository bookRepository;

    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

@GetMapping(value = "/books/{bookid}")
    public String getBook(@PathVariable String bookid, Model model){
    Optional<Book> optionalBook = bookRepository.findById(bookid);

    if(optionalBook.isPresent()){
        Book getbook = optionalBook.get();
        String coverImageUrl = "/images/no-image.png";
        if(getbook.getCoverIds() != null && getbook.getCoverIds().size()>0){
           coverImageUrl = COVER_IMAGE_ROOT + getbook.getCoverIds().get(0) + "-L.jpg";
        }
        model.addAttribute("book", getbook);
        model.addAttribute("coverImage", coverImageUrl);
        return "book";
    }
    return "book-not-found";
    }
}
