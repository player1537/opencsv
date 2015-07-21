/**
 Copyright 2005 Bytecode Pty Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package au.com.bytecode.opencsv;

/**
 * Builder for creating a CSVParser.
 * <br>
 * <pre>
 * {@code
 * final CSVParser parser = new CSVParserBuilder()
 *     .withSeparator('\t')
 *     .withIgnoreQuotations(true)
 *     .build();
 * }
 * </pre>
 *
 * @see CSVParser
 */
public class CSVParserBuilder {

    char separator = CSVParser.DEFAULT_SEPARATOR;
    char quoteChar = CSVParser.DEFAULT_QUOTE_CHARACTER;
    char escapeChar = CSVParser.DEFAULT_ESCAPE_CHARACTER;
    boolean strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;

    boolean ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
    boolean ignoreQuotations = CSVParser.DEFAULT_IGNORE_QUOTATIONS;

    /**
     * Sets the delimiter to use for separating entries
     *
     * @param separator the delimiter to use for separating entries
     * @return this (for method chaining)
     */
    public CSVParserBuilder withSeparator(final char separator) {
        this.separator = separator;
        return this;
    }


    /**
     * Sets the character to use for quoted elements
     *
     * @param quoteChar the character to use for quoted elements
     * @return this (for method chaining)
     */
    public CSVParserBuilder withQuoteChar(final char quoteChar) {
        this.quoteChar = quoteChar;
        return this;
    }


    /**
     * Sets the character to use for escaping a separator or quote
     *
     * @param escapeChar the character to use for escaping a separator or quote
     * @return this (for method chaining)
     */
    public CSVParserBuilder withEscapeChar(final char escapeChar) {
        this.escapeChar = escapeChar;
        return this;
    }


    /**
     * Sets the strict quotes setting - if true, characters
     * outside the quotes are ignored
     *
     * @param strictQuotes if true, characters outside the quotes are ignored
     * @return this (for method chaining)
     */
    public CSVParserBuilder withStrictQuotes(final boolean strictQuotes) {
        this.strictQuotes = strictQuotes;
        return this;
    }

    /**
     * Sets the ignore leading whitespace setting - if true, white space
     * in front of a quote in a field is ignored
     *
     * @param ignoreLeadingWhiteSpace if true, white space in front of a quote in a field is ignored
     * @return this (for method chaining)
     */
    public CSVParserBuilder withIgnoreLeadingWhiteSpace(final boolean ignoreLeadingWhiteSpace) {
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        return this;
    }

    /**
     * Sets the ignore quotations mode - if true, quotations are ignored
     *
     * @param ignoreQuotations if true, quotations are ignored
     * @return this (for method chaining)
     */
    public CSVParserBuilder withIgnoreQuotations(final boolean ignoreQuotations) {
        this.ignoreQuotations = ignoreQuotations;
        return this;
    }

    /**
     * Constructs CSVParser
     *
     * @return A parser with the specified settings
     */
    public CSVParser build() {
        return new CSVParser(
                separator,
                quoteChar,
                escapeChar,
                strictQuotes,
                ignoreLeadingWhiteSpace,
                ignoreQuotations);
    }
}
