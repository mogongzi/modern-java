package me.ryan.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import tup.lucene.ik.IKAnalyzer8x;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteIndex {

    public static void main(String[] args) {
        deleteDoc("title", "美国");
    }

    private static void deleteDoc(String field, String key) {
        Analyzer analyzer = new IKAnalyzer8x();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

        Path indexPath = Paths.get("indexdir");
        try (Directory directory = FSDirectory.open(indexPath)) {
            IndexWriter iw = new IndexWriter(directory, iwc);
            iw.deleteDocuments(new Term(field, key));
            iw.commit();
            iw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
