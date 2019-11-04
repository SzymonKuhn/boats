package com.sda.javagda25.boats.model.dto;

import com.sda.javagda25.boats.model.Boat;
import com.sda.javagda25.boats.model.MeasurePoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualAndMinimumStatesForBoatDto {

    private Boat boat;
    private MeasurePoint measurePoint;
    private Integer minimumValue;
    private Integer warningValue;
    private LocalDateTime measureDateTime;
    private Integer waterState;
    private boolean isDanger;
    private boolean isWarning;


    public static class Builder {
        private Boat boat;
        private MeasurePoint measurePoint;
        private Integer minimumValue;
        private Integer warningValue;
        private LocalDateTime measureDateTime;
        private Integer waterState;
        private boolean isDanger;
        private boolean isWarning;

        public Builder withBoat(Boat boat) {
            this.boat = boat;
            return this;
        }

        public Builder withMeasurePoint (MeasurePoint measurePoint) {
            this.measurePoint = measurePoint;
            return this;
        }

        public Builder withMinimumValue (Integer minimumValue) {
            this.minimumValue = minimumValue;
            return this;
        }

        public Builder withWarningValue (Integer warningValue) {
            this.warningValue = warningValue;
            return this;
        }

        public Builder withMeasureDateTime (LocalDateTime dateTime) {
            this.measureDateTime = dateTime;
            return this;
        }
        public Builder withWaterState (Integer waterState) {
            this.waterState = waterState;
            return this;
        }

        public ActualAndMinimumStatesForBoatDto build () {
            return new ActualAndMinimumStatesForBoatDto(boat, measurePoint, minimumValue, warningValue, measureDateTime, waterState, waterState < minimumValue, waterState < warningValue);
        }

    }

}
