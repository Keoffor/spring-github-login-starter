package io.javabrains.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {
    private final WebClient client;
    private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

    public SearchController(WebClient.Builder client) {
        this.client = client.exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024)).build())
                .baseUrl("http://openlibrary.org/search.json").build();
    }

    @GetMapping(value = "/search")
    public String getSearchResult(@RequestParam String query, Model model){
        Mono<SearchResult> resultMono = this.client.get()
                .uri("?q={query}", query)
                .retrieve().bodyToMono(SearchResult.class);
        SearchResult search = resultMono.block();
        List<SearchResultBook> bookList = search.getDocs()
                        .stream()
                        .limit(10)
                .map(bookresult -> {
                    bookresult.setKey(bookresult.getKey().replace("/works/",""));
                    String coverid = bookresult.getCover_i();
                    if(StringUtils.hasText(coverid)){
                        coverid = COVER_IMAGE_ROOT + coverid + "-M.jpg";
                    }else{
                        coverid = "/images/no-image.png";
                    }
                    bookresult.setCover_i(coverid);
                    return bookresult;
                })
                        .collect(Collectors.toList());
        model.addAttribute("searchRes", bookList);
        return "search";
    }
}
