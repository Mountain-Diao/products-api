package com.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;

@Entity
@Table(name = "timestamps")
public class Timestamps {
    @Id
    private long timestamp_id;

    private String inserted_by;

    private String product_name;

    private Time timing;

    public long getTimestamp_id() {
        return timestamp_id;
    }

    public void setTimestamp_id(long timestamp_id) {
        this.timestamp_id = timestamp_id;
    }

    public String getInserted_by() {
        return inserted_by;
    }

    public void setInserted_by(String inserted_by) {
        this.inserted_by = inserted_by;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Time getTiming() {
        return timing;
    }

    public void setTiming(Time timing) {
        this.timing = timing;
    }
}
