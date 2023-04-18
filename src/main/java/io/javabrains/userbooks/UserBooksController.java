package io.javabrains.userbooks;

import io.javabrains.book.Book;
import io.javabrains.book.BookRepository;
import io.javabrains.user.BooksByUser;
import io.javabrains.user.BooksByUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UserBooksController {
    @Autowired
    UserBooksRepository userBooksRepository;
    @Autowired
    BooksByUserRepository booksByUserRepository;
    @Autowired
    BookRepository bookRepository;

    @PostMapping(value = "/addUserBook")
    public ModelAndView addBookForUser(@RequestBody MultiValueMap<String, String> formData,
                                       @AuthenticationPrincipal OAuth2User principal){
        if(principal==null || principal.getAttribute("login")==null){
          return null;
        }
        String bookId = formData.getFirst("bookId");

        Optional<Book> bookOptional = bookRepository.findById(bookId);
            if(!bookOptional.isPresent()){
                return  new ModelAndView("redirect:/");
            }
        Book book = bookOptional.get();
        UserBooks userBooks = new UserBooks();
        UserBooksPrimaryKey key = new UserBooksPrimaryKey();
        String userId = principal.getAttribute("login");
        int rating = Integer.parseInt(Objects.requireNonNull(formData.getFirst("rating")));
        key.setUserId(userId);
        key.setBookId(bookId);
        userBooks.setKey(key);
        userBooks.setRating(rating);
        userBooks.setReadingStatus(formData.getFirst("readingStatus"));
        userBooks.setStartedDate(LocalDate.parse(Objects.requireNonNull(formData.getFirst("startedDate"))));
        userBooks.setEndDate(LocalDate.parse(Objects.requireNonNull(formData.getFirst("endDate"))));
        userBooksRepository.save(userBooks);

        BooksByUser booksByUser = new BooksByUser();
        booksByUser.setId(userId);
        booksByUser.setBookId(bookId);
        booksByUser.setCoverIds(book.convertCoverIds(book.getCoverIds()));
        booksByUser.setBookName(book.getName());
        booksByUser.setRating(rating);
        booksByUser.setAuthorNames(book.getAuthorNames());
        booksByUser.setReadingStatus(formData.getFirst("readingStatus"));
        booksByUserRepository.save(booksByUser);

        return new ModelAndView("redirect:/books/" + bookId);
    }
}
