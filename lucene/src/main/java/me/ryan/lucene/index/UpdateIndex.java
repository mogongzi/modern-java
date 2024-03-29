package me.ryan.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import tup.lucene.ik.IKAnalyzer8x;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UpdateIndex {

    public static void main(String[] args) {
        Analyzer analyzer = new IKAnalyzer8x();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

        Path indexPath = Paths.get("indexdir");
        try (Directory directory = FSDirectory.open(indexPath)) {
            IndexWriter iw = new IndexWriter(directory, iwc);
            Document doc = new Document();
            doc.add(new TextField("id", "2", Field.Store.YES));
            doc.add(new TextField("title", "北京大学开学迎来4380名新生", Field.Store.YES));
            doc.add(new TextField("content", "昨天，北京大学迎来4380名来自全国各地及数十个国家的本科新生。其中，农村学生共700余名，为近年最多...", Field.Store.YES));
            iw.updateDocument(new Term("title", "北大"), doc);
            iw.commit();
            iw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
