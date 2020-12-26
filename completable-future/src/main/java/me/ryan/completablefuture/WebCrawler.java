package me.ryan.completablefuture;

import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * This WebCrawler example is coming from http://www.nurkiewicz.com/2013/05/java-8-completablefuture-in-action.html
 * The article is about how to use CompletableFuture which provided by JAVA8
 * This example is also mentioned on the book - <Learning Akka> whose author is Jason Goodwin.
 * The section 2.6.1 homework suggest to use the sequence method which implemented by this example.
 */
public class WebCrawler {

    private static final Logger log = LogManager.getLogger(WebCrawler.class);

    private String downloadSite(final String site) {
        try {
            log.debug("Downloading {}", site);
            final String res = IOUtils.toString(new URL(site), StandardCharsets.UTF_8);
            log.info("Done {}", site);
            return res;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private Document parse(String xml) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    private CompletableFuture<Double> calculateRelevance(Document xml) {
        List<String> xmlItems = new ArrayList<>();
        NodeList items = xml.getElementsByTagName("item");

        for (int i = 0; i < items.getLength(); i++) {
            Node element = ((Element) items.item(i)).getElementsByTagName("pubDate").item(0);
            xmlItems.add(element.getTextContent());
        }

        List<ZonedDateTime> itemSum = new ArrayList<>();
        for (String pubDate : xmlItems) {
            ZonedDateTime zd = parseDateTime(pubDate);
            if (zd.isAfter(ZonedDateTime.now().minusDays(4))) {
                itemSum.add(zd);
            }
        }

        log.debug("The number of items: {}", itemSum.size());

        return CompletableFuture.completedFuture(0.0D).thenApply(d -> {
            try {
                int duration = (int) (Math.random() * 3000L);
                log.debug("Sleep duration is: {}", duration);
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return itemSum.size() * 0.1D / 2.5D;
        });
    }

    private ZonedDateTime parseDateTime(String dateTime) {
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("EE, dd MMM yyyy HH:mm:ss Z");
        try {
            return ZonedDateTime.parse(dateTime, dtFormatter);
        } catch (Exception e) {
            log.error("INVALID DATE TIME: {}", dateTime);
            log.error(e);
            return ZonedDateTime.now();
        }
    }

    private static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.<T>toList()));
    }

    private void process() {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<String> topSites = Arrays.asList("https://www.yystv.cn/rss/feed", "https://www.williamlong.info/rss.xml", "https://www.solidot.org/index.rss");

        List<CompletableFuture<Double>> relevanceFutures = topSites.stream()
                .map(site -> CompletableFuture.supplyAsync(() -> downloadSite(site), executor))
                .map(contentFuture -> contentFuture.thenApply(this::parse))
                .map(docFuture -> docFuture.thenCompose(this::calculateRelevance))
                .collect(Collectors.<CompletableFuture<Double>>toList());

        CompletableFuture<List<Double>> allDone = sequence(relevanceFutures);

//        List<Double> scores = allDone.join();
//        scores.forEach(log::info);

        CompletableFuture<OptionalDouble> maxRelevance = allDone
                .thenApply(relevanceList -> relevanceList.stream()
                        .mapToDouble(score -> {
                            log.info("Each score is: {}", score);
                            return score;
                        }).max());

        try {
            log.info("Maximum relevance score is: {}", maxRelevance.get());
            maxRelevance.thenRun(executor::shutdown);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        log.debug(executor.isTerminated());
        try {
            executor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug(executor.isTerminated());
    }

    public static void main(String[] args) {
        WebCrawler webCrawler = new WebCrawler();
        webCrawler.process();
    }
}
