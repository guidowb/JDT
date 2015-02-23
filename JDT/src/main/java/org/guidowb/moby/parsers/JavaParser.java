package org.guidowb.moby.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.guidowb.moby.ast.Block;
import org.guidowb.moby.ast.Statement;

public class JavaParser extends ASTVisitor {

	public static Block parse(String filename) throws IOException { return parse(new File(filename)); }
	public static Block parse(File sourceFile) throws IOException {
		String source = readFile(sourceFile);
		ASTParser astParser = ASTParser.newParser(AST.JLS3);
		astParser.setSource(source.toCharArray());
		CompilationUnit unit = (CompilationUnit) astParser.createAST(null);
		JavaParser javaParser = new JavaParser();
		unit.accept(javaParser);
		return javaParser.getOutermostBlock();
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

	private List<Block> stack = new ArrayList<Block>();
	
	private JavaParser() {
		push();
	}

	private void add(Statement statement) {
		Block currentBlock = stack.get(stack.size() - 1);
		currentBlock.add(statement);
	}
	
	private void push() { stack.add(new Block()); }
	@SuppressWarnings("unused")
	private void pop() { stack.remove(stack.size() - 1); }

	private Block getOutermostBlock() { return stack.get(0); }

	@Override
	public boolean visit(PackageDeclaration decl) {
		Statement statement = new Statement("package");
		statement.put("name", decl.getName().getFullyQualifiedName());
		add(statement);
		return false;
	}

	@Override
	public boolean visit(ImportDeclaration imported) {
		Statement statement = new Statement("import");
		statement.put("name", imported.getName().getFullyQualifiedName());
		add(statement);
		return false;
	}

	@Override
	public boolean visit(TypeDeclaration decl) {
		Statement statement = new Statement(decl.isInterface() ? "interface" : "class");
		statement.put("name", decl.getName().getFullyQualifiedName());
		
		List<String> modifiers = new ArrayList<String>();
		int flags = decl.getModifiers();
		if (Modifier.isPublic(flags)) modifiers.add("public");
		if (Modifier.isProtected(flags)) modifiers.add("protected");
		if (Modifier.isPrivate(flags)) modifiers.add("private");
		if (Modifier.isStatic(flags)) modifiers.add("static");
		if (Modifier.isFinal(flags)) modifiers.add("final");
		if (Modifier.isNative(flags)) modifiers.add("native");
		if (Modifier.isSynchronized(flags)) modifiers.add("synchronized");
		if (Modifier.isAbstract(flags)) modifiers.add("abstract");
		if (Modifier.isTransient(flags)) modifiers.add("transient");
		if (Modifier.isVolatile(flags)) modifiers.add("volatile");
		statement.put("modifiers", modifiers);

		Type superClass = decl.getSuperclassType();
		if (superClass != null)
			if (superClass instanceof SimpleType) statement.put("extends", ((SimpleType) superClass).getName().getFullyQualifiedName());
			else System.err.println("superClass is not a SimpleType but " + superClass.getClass().getSimpleName());

		// TODO - annotations (modifiers?)
		// TODO - interfaces
		// TODO - body
		add(statement);
		return false;
	}

	@Override
	public void preVisit(ASTNode node) {
		String nodeClass = node.getClass().getSimpleName();
		switch (node.getNodeType()) {
		case ASTNode.COMPILATION_UNIT:
		case ASTNode.PACKAGE_DECLARATION:
		case ASTNode.IMPORT_DECLARATION:
			break;
		case ASTNode.TYPE_DECLARATION:
			System.err.println("partial visitor for " + nodeClass + ": " + node.toString());
			break;
		default:
			System.err.println("unimplemented visitor for " + nodeClass + ": " + node.toString());
			break;
		}
	}
}
