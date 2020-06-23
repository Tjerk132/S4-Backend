package nl.fhict.s4.restserver.controllers;

import nl.fhict.s4.restserver.config.exceptions.CustomNotFoundException;
import objects.store.Review;
import org.springframework.web.bind.annotation.*;
import repositories.ReviewRepository;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    public ReviewController() {
        this.repository = new ReviewRepository();
    }

    private ReviewRepository repository;

    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public List<Review> allReviews(){
        return repository.getAll();
    }

    @GetMapping(path = "{id}",
    produces = MediaType.APPLICATION_JSON)
    public Review getReview(@PathVariable Integer id) {
        Review r = repository.getById(id);
        if(r != null) {
            return r;
        }
        else throw new CustomNotFoundException(Review.class.getSimpleName(), id);
    }

    @GetMapping(path = "product/{id}",
    produces = MediaType.APPLICATION_JSON)
    public List<Review> getReviewsByProductId(@PathVariable Integer id) {
        return repository.getByProductId(id);
    }

    @PostMapping(path = "add",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public Review addReview(@RequestBody Review review) {
        repository.add(review);
        return review;
    }

    @DeleteMapping(path = "delete",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public Review deleteReview(@RequestBody Review review) {
        repository.delete(review);
        return review;
    }

    @GetMapping(path = "like",
    produces = MediaType.APPLICATION_JSON)
    public Review likeReview(@RequestParam("userId") int userId , @RequestParam("reviewId") int reviewId) {
        return repository.likeReview(userId, reviewId);
    }
}
