package extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ISBN10Extractor extends AbstractExtractor {

    /**
     * Regex Pattern to find the ISBN-10 in amazon source
     */
    private final Pattern amazonPattern = Pattern.compile("ISBN-10.*ISBN-13");

    /**
     * @param document Document where extraction will occur
     * @param source   The source of the document in order to facilitate the extraction logic
     */
    ISBN10Extractor(String document, Extractor.Source source) {
        super(document, source);
    }

    /**
     * @return The type of extractor in Extractor.Element form
     */
    @Override
    protected Extractor.Element getElementType() {
        return Extractor.Element.ISBN_10;
    }

    /**
     * Logic for extracting element from Amazon source
     *
     * @return Extracted element from source
     */
    @Override
    protected String amazonExtraction() {
        Document doc = Jsoup.parse(document);
        String text = doc.select("div#detail-bullets").first().text();
        Matcher m = amazonPattern.matcher(text);
        String isbn = "";
        if (m.find()) {
            isbn = m.group(0);
            isbn = isbn.replace("ISBN-10: ", "")
                    .replace(" ISBN-13","");
        }
        return isbn;
    }

    /**
     * Logic for extracting element when source is unknown or there is not defined logic
     * @return Extracted element from source
     */
    @Override
    protected String otherExtraction(){
        //TODO:
        return null;
    }
}
