package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.BookRatings;

import com.mycompany.myapp.repository.BookRatingsRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BookRatings.
 */
@RestController
@RequestMapping("/api")
public class BookRatingsResource {

    private final Logger log = LoggerFactory.getLogger(BookRatingsResource.class);
        
    @Inject
    private BookRatingsRepository bookRatingsRepository;

    /**
     * POST  /book-ratings : Create a new bookRatings.
     *
     * @param bookRatings the bookRatings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookRatings, or with status 400 (Bad Request) if the bookRatings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/book-ratings")
    @Timed
    public ResponseEntity<BookRatings> createBookRatings(@Valid @RequestBody BookRatings bookRatings) throws URISyntaxException {
        log.debug("REST request to save BookRatings : {}", bookRatings);
        if (bookRatings.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bookRatings", "idexists", "A new bookRatings cannot already have an ID")).body(null);
        }
        BookRatings result = bookRatingsRepository.save(bookRatings);
        return ResponseEntity.created(new URI("/api/book-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bookRatings", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /book-ratings : Updates an existing bookRatings.
     *
     * @param bookRatings the bookRatings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookRatings,
     * or with status 400 (Bad Request) if the bookRatings is not valid,
     * or with status 500 (Internal Server Error) if the bookRatings couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/book-ratings")
    @Timed
    public ResponseEntity<BookRatings> updateBookRatings(@Valid @RequestBody BookRatings bookRatings) throws URISyntaxException {
        log.debug("REST request to update BookRatings : {}", bookRatings);
        if (bookRatings.getId() == null) {
            return createBookRatings(bookRatings);
        }
        BookRatings result = bookRatingsRepository.save(bookRatings);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bookRatings", bookRatings.getId().toString()))
            .body(result);
    }

    /**
     * GET  /book-ratings : get all the bookRatings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bookRatings in body
     */
    @GetMapping("/book-ratings")
    @Timed
    public List<BookRatings> getAllBookRatings() {
        log.debug("REST request to get all BookRatings");
        List<BookRatings> bookRatings = bookRatingsRepository.findAll();
        return bookRatings;
    }

    /**
     * GET  /book-ratings/:id : get the "id" bookRatings.
     *
     * @param id the id of the bookRatings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookRatings, or with status 404 (Not Found)
     */
    @GetMapping("/book-ratings/{id}")
    @Timed
    public ResponseEntity<BookRatings> getBookRatings(@PathVariable String id) {
        log.debug("REST request to get BookRatings : {}", id);
        BookRatings bookRatings = bookRatingsRepository.findOne(id);
        return Optional.ofNullable(bookRatings)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /book-ratings/:id : delete the "id" bookRatings.
     *
     * @param id the id of the bookRatings to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/book-ratings/{id}")
    @Timed
    public ResponseEntity<Void> deleteBookRatings(@PathVariable String id) {
        log.debug("REST request to delete BookRatings : {}", id);
        bookRatingsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bookRatings", id.toString())).build();
    }

}