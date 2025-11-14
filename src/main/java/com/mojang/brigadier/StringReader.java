package com.mojang.brigadier;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

/* loaded from: KitsX-1.0.3-all.jar:com/mojang/brigadier/StringReader.class */
public class StringReader implements ImmutableStringReader {
    private static final char SYNTAX_ESCAPE = '\\';
    private static final char SYNTAX_DOUBLE_QUOTE = '\"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';
    private final String string;
    private int cursor;

    public StringReader(StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public StringReader(String string) {
        this.string = string;
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public String getString() {
        return this.string;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public int getRemainingLength() {
        return this.string.length() - this.cursor;
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public int getTotalLength() {
        return this.string.length();
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public int getCursor() {
        return this.cursor;
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public String getRead() {
        return this.string.substring(0, this.cursor);
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public String getRemaining() {
        return this.string.substring(this.cursor);
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public boolean canRead(int length) {
        return this.cursor + length <= this.string.length();
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public boolean canRead() {
        return canRead(1);
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public char peek() {
        return this.string.charAt(this.cursor);
    }

    @Override // com.mojang.brigadier.ImmutableStringReader
    public char peek(int offset) {
        return this.string.charAt(this.cursor + offset);
    }

    public char read() {
        String str = this.string;
        int i = this.cursor;
        this.cursor = i + 1;
        return str.charAt(i);
    }

    public void skip() {
        this.cursor++;
    }

    public static boolean isAllowedNumber(char c) {
        return (c >= '0' && c <= '9') || c == '.' || c == '-';
    }

    public static boolean isQuotedStringStart(char c) {
        return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
    }

    public void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip();
        }
    }

    public int readInt() throws CommandSyntaxException {
        int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(this);
        }
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(this, number);
        }
    }

    public long readLong() throws CommandSyntaxException {
        int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedLong().createWithContext(this);
        }
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidLong().createWithContext(this, number);
        }
    }

    public double readDouble() throws CommandSyntaxException {
        int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedDouble().createWithContext(this);
        }
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext(this, number);
        }
    }

    public float readFloat() throws CommandSyntaxException {
        int start = this.cursor;
        while (canRead() && isAllowedNumber(peek())) {
            skip();
        }
        String number = this.string.substring(start, this.cursor);
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedFloat().createWithContext(this);
        }
        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            this.cursor = start;
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidFloat().createWithContext(this, number);
        }
    }

    public static boolean isAllowedInUnquotedString(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || ((c >= 'a' && c <= 'z') || c == '_' || c == '-' || c == '.' || c == '+');
    }

    public String readUnquotedString() {
        int start = this.cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        return this.string.substring(start, this.cursor);
    }

    public String readQuotedString() throws CommandSyntaxException {
        if (!canRead()) {
            return "";
        }
        char next = peek();
        if (!isQuotedStringStart(next)) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedStartOfQuote().createWithContext(this);
        }
        skip();
        return readStringUntil(next);
    }

    public String readStringUntil(char terminator) throws CommandSyntaxException {
        StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (canRead()) {
            char c = read();
            if (escaped) {
                if (c == terminator || c == '\\') {
                    result.append(c);
                    escaped = false;
                } else {
                    setCursor(getCursor() - 1);
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(this, String.valueOf(c));
                }
            } else if (c == '\\') {
                escaped = true;
            } else {
                if (c == terminator) {
                    return result.toString();
                }
                result.append(c);
            }
        }
        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(this);
    }

    public String readString() throws CommandSyntaxException {
        if (!canRead()) {
            return "";
        }
        char next = peek();
        if (isQuotedStringStart(next)) {
            skip();
            return readStringUntil(next);
        }
        return readUnquotedString();
    }

    public boolean readBoolean() throws CommandSyntaxException {
        int start = this.cursor;
        String value = readString();
        if (value.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedBool().createWithContext(this);
        }
        if (value.equals("true")) {
            return true;
        }
        if (value.equals("false")) {
            return false;
        }
        this.cursor = start;
        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidBool().createWithContext(this, value);
    }

    public void expect(char c) throws CommandSyntaxException {
        if (!canRead() || peek() != c) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedSymbol().createWithContext(this, String.valueOf(c));
        }
        skip();
    }
}
