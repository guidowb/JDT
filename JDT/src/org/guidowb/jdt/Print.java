package org.guidowb.jdt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Print {

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.err.println("Usage: Print <filename>...");
			System.exit(1);
		}
		for (int i = 0; i < args.length; i++) {
			CompilationUnit unit = parse(args[i]);
			PrintVisitor visitor = new PrintVisitor();
			unit.accept(visitor);
		}
	}

	public static String readFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try {
			StringBuilder content = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line);
				content.append('\n');
			}
			return content.toString();
		}
		finally {
			if (reader != null) reader.close();
		}
	}

	public static CompilationUnit parse(String filename) throws IOException { return parse(new File(filename)); }
	public static CompilationUnit parse(File sourceFile) throws IOException {
		String source = readFile(sourceFile);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);
		return unit;
	}
}
