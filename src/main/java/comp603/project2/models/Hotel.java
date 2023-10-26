package comp603.project2.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    private int hotelId;
    private String hotelName;
    private String location;
    private int starRating;
    private String description;
    @Override
    public String toString() {
        return this.getHotelName();
    }

}
