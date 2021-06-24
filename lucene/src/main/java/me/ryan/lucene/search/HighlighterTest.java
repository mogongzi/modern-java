package me.ryan.lucene.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import tup.lucene.ik.IKAnalyzer8x;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HighlighterTest {

    public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
        String field = "title";
        Path indexPath = Paths.get("indexdir");
        Directory dir = FSDirectory.open(indexPath);
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new IKAnalyzer8x();
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse("北大");
        System.out.println("Query: " + query);
        QueryScorer score = new QueryScorer(query, field);
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style=\"color:red;\";>", "</span>");
        Highlighter highlight = new Highlighter(formatter, score);

        TopDocs tds = searcher.search(query, 10);
        for (ScoreDoc sd : tds.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            System.out.println("DocID: " + sd.doc);
            System.out.println("Id: " + doc.get("id"));
            System.out.println("Title: " + doc.get("title"));
            TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), sd.doc, field, analyzer);
            Fragmenter fragmenter = new SimpleSpanFragmenter(score);
            highlight.setTextFragmenter(fragmenter);
            String str = highlight.getBestFragment(tokenStream, doc.get(field));
            System.out.println("Highlighted: " + str);
        }
        dir.close();
        reader.close();
    }
}
