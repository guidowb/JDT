package org.guidowb.moby.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

public class JavaParser extends ASTVisitor {

	@SuppressWarnings("serial") public class JSONObject extends LinkedHashMap<String, Object> {}
	@SuppressWarnings("serial") public class JSONArray extends ArrayList<Object> {}
	
	private Map<ASTNode, JSONObject> map = new HashMap<ASTNode, JSONObject>();
	private JSONObject get(ASTNode node) { return map.get(node); }

	@SuppressWarnings("unchecked")
	@Override public void preVisit(ASTNode node) {
		JSONObject json = new JSONObject();
		map.put(node,  json);
		json.put("_class", node.getClass().getSimpleName());
		for (StructuralPropertyDescriptor descriptor : (Iterable<StructuralPropertyDescriptor>) node.structuralPropertiesForType()) {
			if (descriptor.isSimpleProperty()) {
				Object property = node.getStructuralProperty((StructuralPropertyDescriptor) descriptor);
				String value = property.toString();
				if (property instanceof String) value = "'" + value + "'";
				json.put(descriptor.getId(), value);
			}
		}
		ASTNode parent = node.getParent();
		if (parent == null) return;
		StructuralPropertyDescriptor container = node.getLocationInParent();
		if (container == null) return;
		JSONObject parentJson = map.get(parent);
		if (container.isChildProperty()) parentJson.put(container.getId(), json);
		else if (container.isChildListProperty()) {
			JSONArray array = (JSONArray) parentJson.get(container.getId());
			if (array == null) parentJson.put(container.getId(), array = new JSONArray());
			array.add(json);
		}
	}

	public static JSONObject parse(String filename) throws IOException { return parse(new File(filename)); }
	public static JSONObject parse(File sourceFile) throws IOException {
		String source = readFile(sourceFile);
		ASTParser astParser = ASTParser.newParser(AST.JLS3);
		astParser.setSource(source.toCharArray());
		ASTNode node = astParser.createAST(null);
		JavaParser javaParser = new JavaParser();
		node.accept(javaParser);
		print(javaParser.get(node));
		return javaParser.get(node);
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
	
	private static final String indent = "  ";

	public static void print(Object object) { print(object, ""); }
	public static void print(Object object, String prefix) {
		if (object instanceof JSONObject) {
			System.out.println("{");
			for (Entry<String, Object> property : ((JSONObject) object).entrySet()) {
				String name = property.getKey();
				Object value = property.getValue();
				System.out.print(prefix + indent + name + ": ");
				print(value, prefix + indent);
			}
			System.out.println(prefix + "}");
		}
		else if (object instanceof JSONArray) {
			System.out.println("[");
			for (Object member: (JSONArray) object) {
				System.out.print(prefix + indent);
				print(member, prefix + indent);
			}
			System.out.println(prefix + "]");
		}
		else System.out.println(object.toString());
	}
}