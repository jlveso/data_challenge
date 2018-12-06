package extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AuthorExtractor extends AbstractExtractor {

    /**
     * @param document Document where extraction will occur
     * @param source The source of the document in order to facilitate the extraction logic
     */
    AuthorExtractor(String document, Extractor.Source source) {
        super(document, source);
    }

    /**
     * @return The type of extractor in Extractor.Element form
     */
    public Extractor.Element getElementType(){
        return Extractor.Element.Author;
    }

    /**
     * Logic for extracting element from Amazon source
     *
     * @return Extracted element from source
     */
    @Override
    protected String amazonExtraction() {
        Document doc = Jsoup.parse(document);
        Element authorElement = doc.select("h1 + span").first();
        return authorElement.text();
    }

    /**
     * Logic for extracting element when source is unknown or there is not defined logic
     * @return Extracted element from source
     */
    @Override
    protected String otherExtraction() {
        return "";
    }
}
