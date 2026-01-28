package hei.group.exercicetd3;

import org.springframework.boot.convert.PeriodUnit;
import org.springframework.format.annotation.DurationFormat;

import java.time.Instant;

public class StockValue {
    private Double quantity;
    private UnitType uniti;


    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public UnitType getUniti() {
        return uniti;
    }

    public void setUniti(UnitType uniti) {
        this.uniti = uniti;
    }


}
