package io.javabrains.book;

import io.javabrains.userbooks.UserBooks;
import io.javabrains.userbooks.UserBooksPrimaryKey;
import io.javabrains.userbooks.UserBooksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.Optional;

@Controller
public class BookController {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserBooksRepository userBooksRepository;
    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";
    Logger logger = LoggerFactory.getLogger(BookController.class);
@GetMapping(value = "/books/{bookid}")
    public String getBook(@PathVariable String bookid, Model model, @AuthenticationPrincipal OAuth2User principal){
    Optional<Book> optionalBook = bookRepository.findById(bookid);

    if(optionalBook.isEmpty()){
        return "book-not-found";
    }
        Book getbook = optionalBook.get();
        String coverImageUrl = "/images/no-image.png";
        if(getbook.getCoverIds() != null && getbook.getCoverIds().size()>0){
           coverImageUrl = COVER_IMAGE_ROOT + getbook.getCoverIds().get(0) + "-L.jpg";
        }
        getbook.getAuthorIds().forEach(auth -> model.addAttribute("auth", auth));
        getbook.getAuthorNames().forEach(auth -> model.addAttribute("authName", auth));
        model.addAttribute("book", getbook);


        model.addAttribute("coverImage", coverImageUrl);
        if(principal!=null && principal.getAttribute("login")!=null){
            UserBooksPrimaryKey key = new UserBooksPrimaryKey();
            String userId = principal.getAttribute("login");
            model.addAttribute("loginId",userId);
            key.setBookId(getbook.getId());
            key.setUserId(userId);
            Optional<UserBooks> userBooks = userBooksRepository.findById(key);
            if(userBooks.isPresent())
                model.addAttribute("userBook", userBooks.get());
            else
                model.addAttribute("userBook", new UserBooks());

        }
    logger.info("This is printing {} ", optionalBook.get().getPublishedDate());
        return "book";
    }

}
