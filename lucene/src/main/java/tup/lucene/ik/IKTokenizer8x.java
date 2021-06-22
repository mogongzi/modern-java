package tup.lucene.ik;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;

public class IKTokenizer8x extends Tokenizer {

    private IKSegmenter _IKImplement;
    private final CharTermAttribute termAttr;
    private final OffsetAttribute offsetAttr;
    private final TypeAttribute typeAttr;
    private int endPosition;

    public IKTokenizer8x(boolean useSmart) {
        super();
        this.offsetAttr = addAttribute(OffsetAttribute.class);
        this.termAttr = addAttribute(CharTermAttribute.class);
        this.typeAttr = addAttribute(TypeAttribute.class);
        _IKImplement = new IKSegmenter(input, useSmart);
    }


    @Override
    public boolean incrementToken() throws IOException {
        clearAttributes();
        Lexeme nextLexeme = _IKImplement.next();
        if (nextLexeme != null) {
            termAttr.append(nextLexeme.getLexemeText());
            termAttr.setLength(nextLexeme.getLength());
            offsetAttr.setOffset(nextLexeme.getBegin(), nextLexeme.getEndPosition());
            endPosition = nextLexeme.getEndPosition();
            typeAttr.setType(nextLexeme.getLexemeText());

            return true;
        }
        return false;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        _IKImplement.reset(input);
    }

    @Override
    public void end() throws IOException {
        int finalOffset = correctOffset(this.endPosition);
        offsetAttr.setOffset(finalOffset, finalOffset);
    }
}
