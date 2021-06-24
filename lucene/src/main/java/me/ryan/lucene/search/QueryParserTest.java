package me.ryan.lucene.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import tup.lucene.ik.IKAnalyzer8x;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QueryParserTest {

    public static void main(String[] args) {
        String field = "title";
        Path indexPath = Paths.get("indexdir");
        try (Directory dir = FSDirectory.open(indexPath)) {
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new IKAnalyzer8x();
            QueryParser parser = new QueryParser(field, analyzer);
            parser.setDefaultOperator(QueryParser.Operator.AND);
            Query query = parser.parse("农村学生");
            System.out.println("Query: " + query.toString());

            TopDocs tds = searcher.search(query, 10);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                System.out.println("DocID: " + sd.doc);
                System.out.println("Id: " + doc.get("id"));
                System.out.println("Title: " + doc.get("title"));
                System.out.println("Document Score: " + sd.score);
            }
            reader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
