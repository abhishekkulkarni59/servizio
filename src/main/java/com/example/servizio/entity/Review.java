package com.example.servizio.entity;

import com.example.servizio.payload.ReviewDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date reviewDate;

    private String review;

    private Long rating;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ad_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ad ad;

    public ReviewDTO getDto() {
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setId(id);
        reviewDTO.setReviewDate(reviewDate);
        reviewDTO.setReview(review);
        reviewDTO.setRating(rating);
        reviewDTO.setUserId(user.getId());
        reviewDTO.setAdId(ad.getId());
        reviewDTO.setClientName(user.getName());
        reviewDTO.setServiceName(ad.getServiceName());

        return reviewDTO;
    }
}
