package extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TitleExtractor extends AbstractExtractor {

    /**
     * @param document Document where extraction will occur
     * @param source The source of the document in order to facilitate the extraction logic
     */
    TitleExtractor(String document, Extractor.Source source){
        super(document, source);
    }

    /**
     * @return The type of extractor in Extractor.Element form
     */
    public Extractor.Element getElementType(){
        return Extractor.Element.Title;
    }

    /**
     * Logic for extracting element from Amazon source
     *
     * @return Extracted element from source
     */
    @Override
    protected String amazonExtraction(){
        Document doc = Jsoup.parse(document);
        Element titleElement = doc.select("span#btAsinTitle").first();
        return titleElement.text();
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
