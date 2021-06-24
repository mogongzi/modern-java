package me.ryan.lucene.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class IndexDocs {

    public static void main(String[] args) throws IOException {
        File newsFile = new File("testfile/news.txt");
        String text1 = textToString(newsFile);
        Analyzer smcAnalyzer = new SmartChineseAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(smcAnalyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        Directory directory = null;
        IndexWriter indexWriter = null;
        directory = FSDirectory.open(Paths.get("indexdir"));
        indexWriter = new IndexWriter(directory, iwc);

        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        type.setTokenized(true);
        Document doc1 = new Document();
        Field field1 = new Field("content", text1, type);
        doc1.add(field1);
        indexWriter.addDocument(doc1);
        indexWriter.close();
        directory.close();
    }

    private static String textToString(File newsFile) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(newsFile))) {
            String str = null;
            while ((str = reader.readLine()) != null) {
                result.append(System.lineSeparator()).append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
