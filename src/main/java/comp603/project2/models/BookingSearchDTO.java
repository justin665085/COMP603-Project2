package comp603.project2.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingSearchDTO {
    private int roomId;
    private String roomType;
    private double price;
    private int capacity;
    private String available;
}
