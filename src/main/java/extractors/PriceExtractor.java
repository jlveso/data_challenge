package extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PriceExtractor extends AbstractExtractor {

    /**
     * @param document Document where extraction will occur
     * @param source   The source of the document in order to facilitate the extraction logic
     */
    PriceExtractor(String document, Extractor.Source source) {
        super(document, source);
    }

    /**
     * @return The type of extractor in Extractor.Element form
     */
    @Override
    protected Extractor.Element getElementType() {
        return Extractor.Element.Price;
    }

    /**
     * Logic for extracting element from Amazon source
     *
     * @return Extracted element from source
     */
    @Override
    protected String amazonExtraction() {
        Document doc = Jsoup.parse(document);
        String price;
        try {
            price = doc.select("#actualPriceValue").first().text();
        } catch (Exception e){
            price = doc.select("span.rentPrice").first().text();
        }
        price = price.replace("$","").replace(",","");
        return price;
    }

    /**
     * Logic for extracting element when source is unknown or there is not defined logic
     * @return Extracted element from source
     */
    @Override
    protected String otherExtraction() {
        //TODO
        return "";
    }
}
