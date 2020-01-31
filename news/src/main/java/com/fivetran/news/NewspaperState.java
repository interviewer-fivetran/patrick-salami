package com.fivetran.news;

import java.time.LocalDate;
import java.util.Objects;

public class NewspaperState {
    public LocalDate lastDate;
    public Integer lastOffset;

    public NewspaperState() { }

    public NewspaperState(LocalDate lastDate, Integer lastOffset) {
        this.lastDate = lastDate;
        this.lastOffset = lastOffset;
    }

    public static NewspaperState copy(NewspaperState otherState) {
        return new NewspaperState(otherState.lastDate, otherState.lastOffset);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewspaperState that = (NewspaperState) o;
        return Objects.equals(lastDate, that.lastDate) &&
                Objects.equals(lastOffset, that.lastOffset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastDate, lastOffset);
    }

    @Override
    public String toString() {
        return "NewspaperState{" +
                "lastDate=" + lastDate +
                ", lastOffset=" + lastOffset +
                '}';
    }
}
