package org.guidowb.jdt;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class PrintVisitor extends ASTVisitor {

	@Override
	public boolean visit(PackageDeclaration decl) {
		System.out.println("package " + decl.getName() + "\n");
		return false;
	}

	@Override
	public boolean visit(ImportDeclaration imported) {
		System.out.println("import " + imported.getName());
		return false;
	}

	@Override
	public boolean visit(TypeDeclaration decl) {
		if ((decl.getModifiers() & Modifier.PUBLIC) != 0) System.out.print("public ");
		if ((decl.getModifiers() & Modifier.PROTECTED) != 0) System.out.print("protected ");
		if ((decl.getModifiers() & Modifier.PRIVATE) != 0) System.out.print("private ");
		if ((decl.getModifiers() & Modifier.STATIC) != 0) System.out.print("static ");
		if ((decl.getModifiers() & Modifier.FINAL) != 0) System.out.print("final ");
		if ((decl.getModifiers() & Modifier.NATIVE) != 0) System.out.print("native ");
		if ((decl.getModifiers() & Modifier.SYNCHRONIZED) != 0) System.out.print("synchronized ");
		if ((decl.getModifiers() & Modifier.ABSTRACT) != 0) System.out.print("abstract ");
		if ((decl.getModifiers() & Modifier.TRANSIENT) != 0) System.out.print("transient ");
		if ((decl.getModifiers() & Modifier.VOLATILE) != 0) System.out.print("volatile ");
		System.out.print(decl.isInterface() ? "interface " : "class ");
		System.out.print(decl.getName());
		// TODO - type parameters
		Type superClass = decl.getSuperclassType();
		if (superClass != null)
			if (superClass instanceof SimpleType) System.out.print(" extends " + ((SimpleType) superClass).getName());
			else System.err.println("superClass is not a SimpleType but " + superClass.getClass().getSimpleName());
		// TODO - interfaces
		// TODO - body
		return false;
	}

	@Override
	public void preVisit(ASTNode node) {
		switch (node.getNodeType()) {
		case ASTNode.COMPILATION_UNIT:
		case ASTNode.PACKAGE_DECLARATION:
		case ASTNode.IMPORT_DECLARATION:
			break;
		default:
			String nodeClass = node.getClass().getSimpleName();
			System.err.println(nodeClass + ": " + node.toString());
		}
	}
}
