package comp603.project2.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private int reviewId;
    private int userId; // foreign key from User
    private int hotelId; // foreign key from Hotel
    private String reviewText;
    private int rating; // e.g. from 1 to 5
}
