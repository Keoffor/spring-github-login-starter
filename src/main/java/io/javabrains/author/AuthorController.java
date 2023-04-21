package io.javabrains.author;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class AuthorController {
    @Autowired
    AuthorBooksRepository authorBooksRepository;
    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";
    Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @GetMapping(value = "/authors/{authorid}")
    public String getBooksByAuthor(@PathVariable String authorid, Model model ){
       Optional<AuthorBooks> optionalBooks = authorBooksRepository.findAllById(authorid);
       if(optionalBooks.isEmpty()){
           return "index";
       }
       List<AuthorBooks> authorBooks = optionalBooks.stream().map(auth->{
           String coverImageUrl = "/images/no-image.png";
           if(auth.getCoverIds()!=null & auth.getCoverIds().size() > 0){
               coverImageUrl = COVER_IMAGE_ROOT + auth.getCoverIds().get(0)+ "-L.jpg";
               logger.info("This is printing {} ", auth.getPublishedDate());
           }
           auth.setCoverUrl(coverImageUrl);
           return auth;
       }).collect(Collectors.toList());
       model.addAttribute("auths", authorBooks);
        return "authorbooks";
    }
}
