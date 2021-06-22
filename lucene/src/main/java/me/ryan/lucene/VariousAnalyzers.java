package me.ryan.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;
import tup.lucene.ik.IKAnalyzer8x;

import java.io.IOException;
import java.io.StringReader;

public class VariousAnalyzers {
    private static final String str = "中华人民共和国简称中国，是一个有13亿人口的国家";

    public static void main(String[] args) throws IOException {
        Analyzer analyzer = null;
        analyzer = new StandardAnalyzer();
        System.out.println("StandardAnalyzer tokenizing:" + analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer = new WhitespaceAnalyzer();
        System.out.println("WhitespaceAnalyzer tokenizing:" + analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer = new SimpleAnalyzer();
        System.out.println("SimpleAnalyzer tokenizing:" + analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer = new CJKAnalyzer();
        System.out.println("CJKAnalyzer tokenizing:" + analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer = new KeywordAnalyzer();
        System.out.println("KeywordAnalyzer tokenizing:" + analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer = new StopAnalyzer(CharArraySet.EMPTY_SET);
        System.out.println("StopAnalyzer tokenizing:" + analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer = new SmartChineseAnalyzer();
        System.out.println("SmartChineseAnalyzer tokenizing:" + analyzer.getClass());
        printAnalyzer(analyzer);
    }

    private static void printAnalyzer(Analyzer analyzer) throws IOException {
        StringReader reader = new StringReader(str);
        TokenStream tokenStream = analyzer.tokenStream(str, reader);
        tokenStream.reset();
        CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
        System.out.println("Tokenizer result:");
        while (tokenStream.incrementToken()) {
            System.out.print(attribute.toString() + " | ");
        }
        System.out.println("\n");
        analyzer.close();
    }
}
