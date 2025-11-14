package com.mojang.brigadier.suggestion;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.StringRange;
import java.util.Objects;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/suggestion/IntegerSuggestion.class */
public class IntegerSuggestion extends Suggestion {
    private int value;

    public IntegerSuggestion(StringRange range, int value) {
        this(range, value, null);
    }

    public IntegerSuggestion(StringRange range, int value, Message tooltip) {
        super(range, Integer.toString(value), tooltip);
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override // com.mojang.brigadier.suggestion.Suggestion
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntegerSuggestion)) {
            return false;
        }
        IntegerSuggestion that = (IntegerSuggestion) o;
        return this.value == that.value && super.equals(o);
    }

    @Override // com.mojang.brigadier.suggestion.Suggestion
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.value));
    }

    @Override // com.mojang.brigadier.suggestion.Suggestion
    public String toString() {
        return "IntegerSuggestion{value=" + this.value + ", range=" + getRange() + ", text='" + getText() + "', tooltip='" + getTooltip() + "'}";
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.mojang.brigadier.suggestion.Suggestion, java.lang.Comparable
    public int compareTo(Suggestion o) {
        if (o instanceof IntegerSuggestion) {
            return Integer.compare(this.value, ((IntegerSuggestion) o).value);
        }
        return super.compareTo(o);
    }

    @Override // com.mojang.brigadier.suggestion.Suggestion
    public int compareToIgnoreCase(Suggestion b) {
        return compareTo(b);
    }
}
