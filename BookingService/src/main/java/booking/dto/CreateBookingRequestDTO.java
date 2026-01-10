package booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class CreateBookingRequestDTO {
    @JsonProperty("room_id")
    public Long roomId;

    @JsonProperty("auto_select")
    public Boolean autoSelect;

    @JsonProperty("start_date")
    public Date startDate;

    @JsonProperty("end_date")
    public Date endDate;

}
