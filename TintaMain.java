import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class TintaMain {
  static final String outFileName = "Output";

   public static void main(String[] args) throws Exception {
        // create a CharStream that reads from standard input:
      ANTLRInputStream input = new ANTLRInputStream(System.in);
        // create a lexer that feeds off of input CharStream:
      TintaLexer lexer = new TintaLexer(input);
        // create a buffer of tokens pulled from the lexer:
      CommonTokenStream tokens = new CommonTokenStream(lexer);
        // create a parser that feeds off the tokens buffer:
      TintaParser parser = new TintaParser(tokens);
        // begin parsing at inputDescription rule:
      ParseTree tree = parser.inputDescription();
      if (parser.getNumberOfSyntaxErrors() == 0) {
           // print LISP-style tree:
         // System.out.println(tree.toStringTree(parser));
           // listeners:
         ParseTreeWalker walker = new ParseTreeWalker();
         TintaMyListener listener0 = new TintaMyListener();
         walker.walk(listener0, tree);
         PrintWriter fout = new PrintWriter(new File(outFileName+".py"));
         fout.print(listener0.finalCode());
         fout.close();
      }
   }
}
