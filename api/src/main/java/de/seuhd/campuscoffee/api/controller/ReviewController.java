package de.seuhd.campuscoffee.api.controller;

import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.seuhd.campuscoffee.api.dtos.ReviewDto;
import de.seuhd.campuscoffee.api.mapper.DtoMapper;
import de.seuhd.campuscoffee.api.openapi.CrudOperation;
import static de.seuhd.campuscoffee.api.openapi.Operation.CREATE;
import static de.seuhd.campuscoffee.api.openapi.Operation.DELETE;
import static de.seuhd.campuscoffee.api.openapi.Operation.FILTER;
import static de.seuhd.campuscoffee.api.openapi.Operation.GET_ALL;
import static de.seuhd.campuscoffee.api.openapi.Operation.GET_BY_ID;
import static de.seuhd.campuscoffee.api.openapi.Operation.UPDATE;
import static de.seuhd.campuscoffee.api.openapi.Resource.REVIEW;
import de.seuhd.campuscoffee.domain.model.objects.Review;
import de.seuhd.campuscoffee.domain.ports.api.CrudService;
import de.seuhd.campuscoffee.domain.ports.api.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for handling reviews for POS, authored by users.
 */
@Tag(name="Reviews", description="Operations for managing reviews for points of sale.")
@Controller
@RequestMapping("/api/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController extends CrudController<Review, ReviewDto, Long> {
    private final ReviewService reviewService;
    private final DtoMapper<Review, ReviewDto> reviewMapper;

    @Override
    protected @NonNull CrudService<Review, Long> service() {
        return reviewService;
    }

    @Override
    protected @NonNull DtoMapper<Review, ReviewDto> mapper() {
        return reviewMapper;
    }

    @Operation
    @CrudOperation(operation=GET_ALL, resource=REVIEW)
    @GetMapping("")
    public @NonNull ResponseEntity<List<ReviewDto>> getAll() {
        return super.getAll();
    }

    // TODO: Implement the missing methods/endpoints.

    @Operation
    @CrudOperation(operation=GET_BY_ID, resource=REVIEW)
    @GetMapping("/{id}")
    public @NonNull ResponseEntity<ReviewDto> getById(
        @Parameter(description="Unique identifier of the user to retrieve.", required=true)
        @PathVariable Long id) {
            return super.getById(id);
        }

    @Operation
    @CrudOperation(operation=CREATE, resource=REVIEW)
    @PostMapping("")
    public @NonNull ResponseEntity<ReviewDto> create(
            @Parameter(description="Data of the review to create.", required=true)
            @RequestBody @Valid ReviewDto reviewDto) {
        return super.create(reviewDto);
    }

    @Operation
    @CrudOperation(operation=UPDATE, resource=REVIEW)
    @PutMapping("/{id}")
    public @NonNull ResponseEntity<ReviewDto> update(
            @Parameter(description="Unique identifier of the review to update.", required=true)
            @PathVariable Long id,
            @Parameter(description="Data of the review to update.", required=true)
            @RequestBody @Valid ReviewDto reviewDto) {
        return super.update(id, reviewDto);
    }

    @Operation
    @CrudOperation(operation=DELETE, resource=REVIEW)
    @DeleteMapping("/{id}")
    public @NonNull ResponseEntity<Void> delete(
            @Parameter(description="Unique identifier of the review to delete.", required=true)
            @PathVariable Long id) {
        return super.delete(id);
    }

    //get review by user id
    @Operation
    @CrudOperation(operation=FILTER, resource=REVIEW)
    @GetMapping("/filter")
    public ResponseEntity<List<ReviewDto>> filter(
            @Parameter(description="Pos id of the reviews to retrieve.", required=true)
            @RequestParam("pos_id") Long posId,
            @RequestParam("approved") Boolean approved){
        List<Review> reviews = reviewService.filter(posId, approved);
        return ResponseEntity.ok(
            reviews.stream().map(reviewMapper::fromDomain).toList());
    }

    @Operation
    //@CrudOperation(operation=APPROVE, resource=REVIEW)
    @PostMapping("/{id}/approve")
    public ResponseEntity<ReviewDto> approve(
        @PathVariable Long id,
        @RequestParam("user_id") Long userId){
            Review review = reviewService.getById(id);
            Review updated = reviewService.approve(review, userId);
            return ResponseEntity.ok(
                reviewMapper.fromDomain(updated));
        }
}
