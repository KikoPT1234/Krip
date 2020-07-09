package pt.kiko.krip.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.cases.Case;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.nodes.*;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Interpreter is responsible for executing the parsed code
 */
final public class Interpreter {

	/**
	 * This will "visit" the specified Node instance
	 *
	 * @param node    The node to "visit"
	 * @param context The context to run in
	 * @return A RuntimeResult instance
	 */
	public static RuntimeResult visit(Node node, Context context) {
		if (node == null) return new RuntimeResult().success(new KripNull(context));
		else if (node instanceof NumberNode) return visitNumberNode((NumberNode) node, context);
		else if (node instanceof StringNode) return visitStringNode((StringNode) node, context);
		else if (node instanceof ListNode) return visitListNode((ListNode) node, context);
		else if (node instanceof BinaryOperationNode)
			return visitBinaryOperationNode((BinaryOperationNode) node, context);
		else if (node instanceof UnaryOperationNode) return visitUnaryOperationNode((UnaryOperationNode) node, context);
		else if (node instanceof IfNode) return visitIfNode((IfNode) node, context);
		else if (node instanceof ForNode) return visitForNode((ForNode) node, context);
		else if (node instanceof WhileNode) return visitWhileNode((WhileNode) node, context);
		else if (node instanceof ObjectNode) return visitObjectNode((ObjectNode) node, context);
		else if (node instanceof ObjectAccessNode) return visitObjectAccessNode((ObjectAccessNode) node, context);
		else if (node instanceof ObjectAssignNode) return visitObjectAssignNode((ObjectAssignNode) node, context);
		else if (node instanceof VarAccessNode) return visitVarAccessNode((VarAccessNode) node, context);
		else if (node instanceof VarCreateNode) return visitVarCreateNode((VarCreateNode) node, context);
		else if (node instanceof VarAssignNode) return visitVarAssignNode((VarAssignNode) node, context);
		else if (node instanceof FunctionNode) return visitFunctionNode((FunctionNode) node, context);
		else if (node instanceof CallNode) return visitCallNode((CallNode) node, context);
		else if (node instanceof TryCatchNode) return visitTryCatchNode((TryCatchNode) node, context);
		else if (node instanceof ReturnNode) return visitReturnNode((ReturnNode) node, context);
		else if (node instanceof ContinueNode) return visitContinueNode();
		else if (node instanceof BreakNode) return visitBreakNode();
		else if (node instanceof EmptyNode)
			return new RuntimeResult().success(new KripNull(context).setPosition(node.startPosition, node.endPosition));
		else
			return new RuntimeResult().failure(new RuntimeError(node.startPosition, node.endPosition, "Unexpected node", context));
	}

	private static RuntimeResult visitNumberNode(@NotNull NumberNode node, Context context) {
		return new RuntimeResult().success(new KripNumber(Double.parseDouble(node.token.value), context).setPosition(node.startPosition, node.endPosition));
	}

	private static RuntimeResult visitStringNode(@NotNull StringNode node, Context context) {
		return new RuntimeResult().success(new KripString(node.token.value, context).setPosition(node.startPosition, node.endPosition));
	}

	private static RuntimeResult visitListNode(@NotNull ListNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		ArrayList<KripValue<?>> elements = new ArrayList<>();

		for (Node elementNode : node.nodes) {
			KripValue<?> element = result.register(visit(elementNode, context));
			if (result.shouldReturn()) return result;
			elements.add(element);
		}

		return result.success(new KripList(elements, context).setPosition(node.startPosition, node.endPosition));
	}

	private static RuntimeResult visitBinaryOperationNode(@NotNull BinaryOperationNode node, Context context) {
		RuntimeResult result = new RuntimeResult();

		KripValue<?> left = result.register(visit(node.left, context));
		if (result.shouldReturn()) return result;

		KripValue<?> right = result.register(visit(node.right, context));
		if (result.shouldReturn()) return result;

		if (node.operationToken.matches(TokenTypes.PLUS)) {
			KripValue<?> operationResult = result.register(left.plus(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.MINUS)) {
			KripValue<?> operationResult = result.register(left.minus(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.MUL)) {
			KripValue<?> operationResult = result.register(left.mul(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.DIV)) {
			KripValue<?> operationResult = result.register(left.div(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.POW)) {
			KripValue<?> operationResult = result.register(left.pow(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.MOD)) {
			KripValue<?> operationResult = result.register(left.mod(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.EE)) {
			KripValue<?> operationResult = result.register(left.equal(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.NE)) {
			KripValue<?> operationResult = result.register(left.notEquals(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.LT)) {
			KripValue<?> operationResult = result.register(left.lessThan(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.LTE)) {
			KripValue<?> operationResult = result.register(left.lessThanOrEqual(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.GT)) {
			KripValue<?> operationResult = result.register(left.greaterThan(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.GTE)) {
			KripValue<?> operationResult = result.register(left.greaterThanOrEqual(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.AND)) {
			KripValue<?> operationResult = result.register(left.and(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.OR)) {
			KripValue<?> operationResult = result.register(left.or(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else return left.illegalOperation(right);
	}

	private static RuntimeResult visitUnaryOperationNode(@NotNull UnaryOperationNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> value = result.register(visit(node.node, context)).setPosition(node.startPosition, node.endPosition);
		if (result.shouldReturn()) return result;

		if (node.operationToken.matches(TokenTypes.MINUS)) return value.mul(new KripNumber(-1, context));
		else if (node.operationToken.matches(TokenTypes.NOT))
			return result.success(new KripBoolean(!value.isTrue(), context));
		else return result.success(value);
	}

	private static RuntimeResult visitIfNode(@NotNull IfNode node, Context context) {
		RuntimeResult result = new RuntimeResult();

		for (Case ifCase : node.cases) {
			Node condition = ifCase.condition;
			Node body = ifCase.body;
			boolean shouldReturnNull = !ifCase.isExpression;

			KripValue<?> conditionValue = result.register(visit(condition, context));
			if (result.shouldReturn()) return result;

			if (conditionValue.isTrue()) {
				KripValue<?> expressionValue = result.register(visit(body, context));
				if (result.shouldReturn()) return result;

				return result.success(shouldReturnNull ? new KripNull(context) : expressionValue);
			}
		}

		if (node.elseCase != null) {
			KripValue<?> elseValue = result.register(visit(node.elseCase.body, context));
			if (result.shouldReturn()) return result;

			return result.success(node.elseCase.isExpression ? elseValue : new KripNull(context));
		}
		return result.success(new KripNull(context));
	}

	private static RuntimeResult visitObjectNode(@NotNull ObjectNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		Map<String, KripValue<?>> valueObj = new HashMap<>();
		KripObject objectValue = new KripObject(valueObj, context);

		for (Map.Entry<String, Node> value : node.object.entrySet()) {
			KripValue<?> objValue = result.register(visit(value.getValue(), context));
			if (result.shouldReturn()) return result;

			if (objValue instanceof KripBaseFunction) {
				((KripBaseFunction) objValue).setThis(objectValue);
				((KripBaseFunction) objValue).setValue(value.getKey());
			}

			valueObj.put(value.getKey(), objValue);
		}

		objectValue.setPosition(node.startPosition, node.endPosition);
		return result.success(objectValue);
	}

	private static RuntimeResult visitObjectAccessNode(@NotNull ObjectAccessNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> object = result.register(visit(node.objectNode, context));
		if (result.shouldReturn()) return result;

		String key = node.keyToken != null ? node.keyToken.value : result.register(visit(node.keyNode, context)).getValueString();
		Position startPosition = node.keyToken != null ? node.keyToken.startPosition : node.keyNode.startPosition;
		Position endPosition = node.keyToken != null ? node.keyToken.endPosition : node.keyNode.endPosition;
		KripValue<?> returnValue = null;

		if (object instanceof KripObject) {

			if (result.shouldReturn()) return result;

			returnValue = ((KripObject) object).get(key);
		} else if (object instanceof KripList) {
			KripValue<?> keyValue = result.register(visit(node.keyNode, context));
			if (result.shouldReturn()) return result;

			if (keyValue instanceof KripNumber) {
				returnValue = ((KripList) object).value.get(Integer.parseInt(keyValue.getValueString()));
			}
		} else if (object instanceof KripNull)
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, "Cannot read property '" + key + "' of null", context));

		if (returnValue == null) {
			object.makePrototype();
			returnValue = object.prototype.get(key);
		}

		return result.success(returnValue != null ? returnValue.setPosition(startPosition, endPosition) : new KripNull(context).setPosition(startPosition, endPosition));
	}

	private static RuntimeResult visitObjectAssignNode(@NotNull ObjectAssignNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> object = result.register(visit(node.objectNode, context));
		if (result.shouldReturn()) return result;

		if (!(object instanceof KripObject || object instanceof KripList))
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, "Not an object/list", context));

		if (node.keyToken != null && object instanceof KripList)
			return result.failure(new RuntimeError(node.keyToken.startPosition, node.keyToken.endPosition, "Not an object", context));

		KripValue<?> value = result.register(visit(node.valueNode, context));
		if (result.shouldReturn()) return result;

		if (object instanceof KripObject) {
			String key = node.keyNode == null ? node.keyToken.value : result.register(visit(node.keyNode, context)).getValueString();
			if (result.shouldReturn()) return result;

			if (value instanceof KripBaseFunction) ((KripBaseFunction) value).setValue(key);
			((KripObject) object).set(key, value);
		} else {
			KripValue<?> keyValue = result.register(visit(node.keyNode, context));
			if (result.shouldReturn()) return result;

			if (!(keyValue instanceof KripNumber) || keyValue.getValueString().contains(".") || Integer.parseInt(keyValue.getValueString()) < 0 || Integer.parseInt(keyValue.getValueString()) > ((KripList) object).value.size())
				return result.success(new KripNull(context));

			if (Integer.parseInt(keyValue.getValueString()) == ((KripList) object).value.size())
				((KripList) object).value.add(value);
			else ((KripList) object).value.set(Integer.parseInt(keyValue.getValueString()), value);

		}
		return result.success(object);
	}

	private static RuntimeResult visitVarAccessNode(@NotNull VarAccessNode node, @NotNull Context context) {
		RuntimeResult result = new RuntimeResult();
		String varName = node.token.value;
		KripValue<?> value = context.symbolTable.get(varName);

		if (value == null)
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, varName + " is not defined", context));

		value = value.setPosition(node.startPosition, node.endPosition);
		return result.success(value);
	}

	private static RuntimeResult visitVarCreateNode(@NotNull VarCreateNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		String varName = node.token.value;

		KripValue<?> value = result.register(visit(node.expression, context));
		if (result.shouldReturn()) return result;

		if (value == null)
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, varName + " is not defined", context));

		value.setPosition(node.token.startPosition, node.token.endPosition);

		if (context.symbolTable.has(varName))
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, "This variable already exists", context));

		context.symbolTable.set(varName, value, node.isFinal);
		return result.success(value);
	}

	private static RuntimeResult visitVarAssignNode(@NotNull VarAssignNode node, Context context) {
		RuntimeResult result = new RuntimeResult();

		String varName = node.token.value;

		KripValue<?> value = result.register(visit(node.expression, context)).setPosition(node.token.startPosition, node.token.endPosition);
		if (result.shouldReturn()) return result;

		if (Krip.globals.containsKey(varName)) {
			Krip.globals.remove(varName);
			Krip.globals.put(varName, value);
			Krip.context.symbolTable.set(varName, value, false);
		} else {
			String setState = context.symbolTable.setExisting(varName, value);

			if (!setState.equals("success")) {
				if (setState.equals("not found")) {
					Krip.globals.put(varName, value);
					Krip.context.symbolTable.set(varName, value, false);
				} else
					return result.failure(new RuntimeError(node.startPosition, node.endPosition, "Variable is constant", context));
			}
		}

		return result.success(value);
	}

	private static @NotNull RuntimeResult visitForNode(@NotNull ForNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		List<KripValue<?>> elements = new ArrayList<>();

		Context forContext1 = new Context("for loop", context);
		forContext1.symbolTable = new SymbolTable(context.symbolTable);

		result.register(visit(node.declaration, forContext1));
		if (result.shouldReturn()) return result;

		while (true) {
			KripValue<?> condition = result.register(visit(node.condition, forContext1));
			if (result.shouldReturn()) return result;
			if (!condition.isTrue()) break;

			Context forContext2 = new Context("for loop", context);
			forContext2.symbolTable = new SymbolTable(forContext1.symbolTable);

			KripValue<?> value = result.register(visit(node.statements, forContext2));
			if (result.shouldReturn()) return result;

			if (result.loopShouldContinue) continue;
			if (result.loopShouldBreak) break;
			elements.add(value);

			result.register(visit(node.execution, forContext1));
			if (result.shouldReturn()) return result;
		}

		return result.success(node.shouldReturnNull ? new KripNull(context) : new KripList(elements, context).setPosition(node.startPosition, node.endPosition));
	}

	private static RuntimeResult visitWhileNode(@NotNull WhileNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		List<KripValue<?>> elements = new ArrayList<>();

		while (true) {
			KripValue<?> condition = result.register(visit(node.conditionNode, context));
			if (result.shouldReturn()) return result;

			if (!condition.isTrue()) break;

			Context whileContext = new Context("while loop", context);
			whileContext.symbolTable = new SymbolTable(context.symbolTable);

			KripValue<?> value = result.register(visit(node.bodyNode, whileContext));

			if (result.shouldReturn() && !result.loopShouldContinue && !result.loopShouldBreak) return result;

			if (result.loopShouldContinue) continue;
			if (result.loopShouldBreak) break;
			elements.add(value);
		}

		return result.success(node.shouldReturnNull ? new KripNull(context) : new KripList(elements, context).setPosition(node.startPosition, node.endPosition));
	}

	@Contract(pure = true)
	private static RuntimeResult visitFunctionNode(@NotNull FunctionNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		String functionName = (node.varNameToken != null && node.varNameToken.value != null) ? node.varNameToken.value : null;

		Node body = node.bodyNode;

		List<String> argNames = new ArrayList<>();
		node.argNameTokens.forEach((token) -> argNames.add(token.value));

		KripValue<?> functionValue = new KripFunction(functionName, body, argNames, node.shouldAutoReturn, context).setPosition(node.startPosition, node.endPosition);

		boolean success = true;
		if (node.varNameToken != null) success = context.symbolTable.set(functionName, functionValue, false);
		if (!success)
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, "Cannot assign to constant variable", context));

		return result.success(functionValue);
	}

	private static RuntimeResult visitCallNode(@NotNull CallNode node, Context context) {
		RuntimeResult result = new RuntimeResult();

		KripValue<?> valueToCall = result.register(visit(node.nodeToCall, context));
		if (result.shouldReturn()) return result;

		if (!(valueToCall instanceof KripBaseFunction))
			return result.failure(new RuntimeError(valueToCall.startPosition, valueToCall.endPosition, "Type '" + valueToCall.getType() + "' is not a function", context));
		valueToCall.setPosition(node.startPosition, node.endPosition);

		Context callContext = new Context("<arguments>", context, node.startPosition);
		callContext.symbolTable = new SymbolTable(context.symbolTable);
		List<KripValue<?>> args = node.argNodes.stream().map(arg -> result.register(visit(arg, callContext))).collect(Collectors.toList());
		if (result.shouldReturn()) return result;

		KripValue<?> returnValue = result.register(((KripBaseFunction) valueToCall).execute(args, context));
		if (result.shouldReturn()) return result;
		returnValue.setContext(context).setPosition(node.startPosition, node.endPosition);

		return result.success(returnValue);
	}

	private static RuntimeResult visitTryCatchNode(@NotNull TryCatchNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		Context tryContext = new Context("<try>", context);
		tryContext.symbolTable = new SymbolTable(context.symbolTable);

		result.register(visit(node.tryNode, tryContext));
		if (result.error != null) {
			KripError errorValue = new KripError(result.error, node.startPosition, node.endPosition, context);

			Context catchContext = new Context("<catch>", context);
			catchContext.symbolTable = new SymbolTable(context.symbolTable);
			if (node.errorNameToken != null) catchContext.symbolTable.set(node.errorNameToken.value, errorValue, false);

			result.error = null;

			result.register(visit(node.catchNode, catchContext));
			if (result.shouldReturn()) return result;
		}

		if (node.finallyNode != null) {
			Context finallyContext = new Context("<finally>", context);
			finallyContext.symbolTable = new SymbolTable(context.symbolTable);

			result.register(visit(node.finallyNode, finallyContext));

			if (result.shouldReturn()) return result;
		}

		return result.success(new KripNull(context));
	}

	private static RuntimeResult visitReturnNode(@NotNull ReturnNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> value = new KripNull(context);

		if (node.nodeToReturn != null) {
			value = result.register(visit(node.nodeToReturn, context));
			if (result.shouldReturn()) return result;
		}

		return result.successReturn(value);
	}

	private static RuntimeResult visitContinueNode() {
		return new RuntimeResult().successContinue();
	}

	private static RuntimeResult visitBreakNode() {
		return new RuntimeResult().successBreak();
	}

}
