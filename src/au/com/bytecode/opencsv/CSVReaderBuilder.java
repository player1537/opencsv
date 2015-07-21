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


import java.io.Reader;

/**
 * Builder for creating a CSVReader.
 * <br>
 * <code>
 * final CSVParser parser =
 * new CSVParserBuilder()
 * .withSeparator('\t')
 * .withIgnoreQuotations(true)
 * .build();
 * final CSVReader reader =
 * new CSVReaderBuilder(new StringReader(csv))
 * .withSkipLines(1)
 * .withCSVParser(parser)
 * .build();
 * </code>
 *
 * @see CSVReader
 */
public class CSVReaderBuilder {

    final Reader reader;
    int skipLines = CSVReader.DEFAULT_SKIP_LINES;
    CSVParserBuilder csvParserBuilder = new CSVParserBuilder();
    /*@Nullable*/ CSVParser csvParser = null;

    /**
     * Sets the reader to an underlying CSV source
     *
     * @param reader the reader to an underlying CSV source.
     */
    CSVReaderBuilder(
            final Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader may not be null");
        }
        this.reader = reader;
    }

    /**
     * Sets the line number to skip for start reading
     *
     * @param skipLines the line number to skip for start reading
     */
    CSVReaderBuilder withSkipLines(
            final int skipLines) {
        this.skipLines = (skipLines <= 0 ? 0 : skipLines);
        return this;
    }


    /**
     * Sets the parser to use to parse the input
     *
     * @param csvParser the parser to use to parse the input
     */
    CSVReaderBuilder withCSVParser(final /*@Nullable*/ CSVParser csvParser) {
        this.csvParser = csvParser;
        return this;
    }


    /**
     * Sets the delimiter to use for separating entries
     *
     * @param separator the delimiter to use for separating entries
     */
    CSVReaderBuilder withSeparator(final char separator) {
        csvParserBuilder.withSeparator(separator);
        return this;
    }


    /**
     * Sets the character to use for quoted elements
     *
     * @param quoteChar the character to use for quoted elements
     */
    CSVReaderBuilder withQuoteChar(final char quoteChar) {
        csvParserBuilder.withQuoteChar(quoteChar);
        return this;
    }


    /**
     * Sets the character to use for escaping a separator or quote
     *
     * @param escapeChar the character to use for escaping a separator or quote
     */
    CSVReaderBuilder withEscapeChar(final char escapeChar) {
        csvParserBuilder.withEscapeChar(escapeChar);
        return this;
    }


    /**
     * Sets the strict quotes setting - if true, characters
     * outside the quotes are ignored
     *
     * @param strictQuotes if true, characters outside the quotes are ignored
     */
    CSVReaderBuilder withStrictQuotes(final boolean strictQuotes) {
        csvParserBuilder.withStrictQuotes(strictQuotes);
        return this;
    }

    /**
     * Sets the ignore leading whitespace setting - if true, white space
     * in front of a quote in a field is ignored
     *
     * @param ignoreLeadingWhiteSpace if true, white space in front of a quote in a field is ignored
     */
    CSVReaderBuilder withIgnoreLeadingWhiteSpace(final boolean ignoreLeadingWhiteSpace) {
        csvParserBuilder.withIgnoreLeadingWhiteSpace(ignoreLeadingWhiteSpace);
        return this;
    }

    /**
     * Sets the ignore quotations mode - if true, quotations are ignored
     *
     * @param ignoreQuotations if true, quotations are ignored
     */
    CSVReaderBuilder withIgnoreQuotations(final boolean ignoreQuotations) {
        csvParserBuilder.withIgnoreQuotations(ignoreQuotations);
        return this;
    }

    /**
     * Constructs CSVReader
     */
    CSVReader build() {
        final CSVParser parser = (csvParser != null) ? csvParser : csvParserBuilder.build();
        return new CSVReader(reader, skipLines, parser);
    }
}
