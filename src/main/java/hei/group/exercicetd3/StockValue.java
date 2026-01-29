package hei.group.exercicetd3;

import org.springframework.boot.convert.PeriodUnit;
import org.springframework.format.annotation.DurationFormat;

import java.time.Instant;
import java.util.Objects;

public class StockValue {
    private Double quantity;
    private UnitType uniti;

    public StockValue(Double quantity, UnitType uniti) {
        this.quantity = quantity;
        this.uniti = uniti;
    }
    public StockValue() {
        this.quantity = quantity;
        this.uniti = uniti;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockValue that = (StockValue) o;
        return Objects.equals(quantity, that.quantity) && uniti == that.uniti;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, uniti);
    }
}
