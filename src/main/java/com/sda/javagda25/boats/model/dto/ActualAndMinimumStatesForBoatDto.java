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

    private Long boatId;
    private MeasurePoint measurePoint;
    private Integer minimumValue;
    private Integer warningValue;
    private LocalDateTime measureDateTime;
    private Integer waterState;
    private boolean isDanger;
    private boolean isWarning;
    private String lng;
    private String lat;


    public static class Builder {
        private Long boatId;
        private MeasurePoint measurePoint;
        private Integer minimumValue;
        private Integer warningValue;
        private LocalDateTime measureDateTime;
        private Integer waterState;
        private boolean isDanger;
        private boolean isWarning;
        private String lng;
        private String lat;

        public Builder withBoatId(Long boatId) {
            this.boatId = boatId;
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
        public Builder withLng (String lng) {
            this.lng = lng;
            return this;
        }
        public Builder withLat (String lat) {
            this.lat = lat;
            return this;
        }



        public ActualAndMinimumStatesForBoatDto build () {
            return new ActualAndMinimumStatesForBoatDto(boatId, measurePoint, minimumValue, warningValue, measureDateTime,
                    waterState, waterState < minimumValue, waterState < warningValue, lng, lat);
        }

    }

}
