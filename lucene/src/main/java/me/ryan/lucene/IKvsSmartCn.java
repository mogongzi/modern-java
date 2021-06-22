package me.ryan.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import tup.lucene.ik.IKAnalyzer8x;

import java.io.IOException;
import java.io.StringReader;

public class IKvsSmartCn {

    private static String str1 = "公路局正在治理解放大道路面积水问题。";
    private static String str2 = "IKAnalyzer是一个开源的，基于java语言开发的轻量级的中文分词工具包。";

    public static void main(String[] args) throws IOException {
        System.out.println("句子一：" + str1);
        Analyzer analyzer = null;
        analyzer = new SmartChineseAnalyzer();
        System.out.println("SmartChineseAnalyzer tokenizing:" + analyzer.getClass());
        printAnalyzer(analyzer, str1);
        analyzer = new IKAnalyzer8x(true);
        System.out.println("IKAnalyzer tokenizing:" + analyzer.getClass());
        printAnalyzer(analyzer, str1);
    }

    private static void printAnalyzer(Analyzer analyzer, String str) throws IOException {
        StringReader reader = new StringReader(str);
        TokenStream tokenStream = analyzer.tokenStream(str, reader);
        tokenStream.reset();
        CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
        System.out.println("Tokenizer result:");
        while (tokenStream.incrementToken()) {
            System.out.print(attribute.toString() + " | ");
        }
        System.out.println("\n");
    }
}
