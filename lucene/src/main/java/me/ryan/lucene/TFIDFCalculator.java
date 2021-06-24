package me.ryan.lucene;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class TFIDFCalculator {

    public static final Logger log = LogManager.getLogger(TFIDFCalculator.class);

    public double tf(List<String> doc, String term) {
        var termFrequency = 0.0D;

        for (String word : doc) {
            if (word.equalsIgnoreCase(term)) {
                termFrequency++;
            }
        }

        return termFrequency / doc.size();
    }

    public int df(List<List<String>> docs, String term) {
        var n = 0;
        if (term != null && !term.equals("")) {
            for (List<String> doc : docs) {
                for (String word : doc) {
                    if (term.equalsIgnoreCase(word)) {
                        n++;
                        break;
                    }
                }
            }
        } else {
            log.info("Term cannot be null or empty.");
        }

        return n;
    }

    public double idf(List<List<String>> docs, String term) {
        return Math.log(docs.size() / ((double)df(docs, term) + 1));
    }

    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);
    }

    public static void main(String[] args) {
        List<String> doc1 = Arrays.asList("人工", "智能", "成为", "互联网", "大会", "焦点");
        List<String> doc2 = Arrays.asList("谷歌", "推出", "开源", "人工", "智能", "系统", "工具");
        List<String> doc3 = Arrays.asList("互联网", "的", "未来", "在", "人工", "智能");
        List<String> doc4 = Arrays.asList("谷歌", "开源", "机器", "学习", "工具");

        List<List<String>> docs = Arrays.asList(doc1, doc2, doc3, doc4);

        var calculator = new TFIDFCalculator();
        log.info(calculator.tf(doc2, "谷歌"));
        log.info(calculator.df(docs, "谷歌"));
        double tfidf = calculator.tfIdf(doc2, docs, "谷歌");
        if (log.isInfoEnabled()) {
            log.info(String.format("TF-IDF(谷歌) = %s", tfidf));
        }
    }
}
