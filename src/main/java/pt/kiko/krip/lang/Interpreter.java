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
		if (node == null) return new RuntimeResult().success(new NullValue(context));
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
		else if (node instanceof ReturnNode) return visitReturnNode((ReturnNode) node, context);
		else if (node instanceof ContinueNode) return visitContinueNode();
		else if (node instanceof BreakNode) return visitBreakNode();
		else if (node instanceof EmptyNode)
			return new RuntimeResult().success(new NullValue(context).setPosition(node.startPosition, node.endPosition));
		else
			return new RuntimeResult().failure(new RuntimeError(node.startPosition, node.endPosition, "Unexpected node", context));
	}

	private static RuntimeResult visitNumberNode(@NotNull NumberNode node, Context context) {
		return new RuntimeResult().success(new NumberValue(Double.parseDouble(node.token.value), context).setPosition(node.startPosition, node.endPosition));
	}

	private static RuntimeResult visitStringNode(@NotNull StringNode node, Context context) {
		return new RuntimeResult().success(new StringValue(node.token.value, context).setPosition(node.startPosition, node.endPosition));
	}

	private static RuntimeResult visitListNode(@NotNull ListNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		ArrayList<Value<?>> elements = new ArrayList<>();
		Node[] nodes = {};
		nodes = node.nodes.toArray(nodes);

		for (Node elementNode : nodes) {
			Value<?> element = result.register(visit(elementNode, context));
			if (result.shouldReturn()) return result;
			elements.add(element);
		}

		return result.success(new ListValue(elements, context).setPosition(node.startPosition, node.endPosition));
	}

	private static RuntimeResult visitBinaryOperationNode(@NotNull BinaryOperationNode node, Context context) {
		RuntimeResult result = new RuntimeResult();

		Value<?> left = result.register(visit(node.left, context));
		if (result.shouldReturn()) return result;

		Value<?> right = result.register(visit(node.right, context));
		if (result.shouldReturn()) return result;

		if (node.operationToken.matches(TokenTypes.PLUS)) {
			Value<?> operationResult = result.register(left.plus(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.MINUS)) {
			Value<?> operationResult = result.register(left.minus(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.MUL)) {
			Value<?> operationResult = result.register(left.mul(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.DIV)) {
			Value<?> operationResult = result.register(left.div(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.POW)) {
			Value<?> operationResult = result.register(left.pow(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.MOD)) {
			Value<?> operationResult = result.register(left.mod(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.EE)) {
			Value<?> operationResult = result.register(left.equal(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.NE)) {
			Value<?> operationResult = result.register(left.notEquals(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.LT)) {
			Value<?> operationResult = result.register(left.lessThan(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.LTE)) {
			Value<?> operationResult = result.register(left.lessThanOrEqual(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.GT)) {
			Value<?> operationResult = result.register(left.greaterThan(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.GTE)) {
			Value<?> operationResult = result.register(left.greaterThanOrEqual(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.AND)) {
			Value<?> operationResult = result.register(left.and(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else if (node.operationToken.matches(TokenTypes.OR)) {
			Value<?> operationResult = result.register(left.or(right));
			if (result.shouldReturn()) return result;
			return result.success(operationResult.setContext(context));
		} else return left.illegalOperation(right);
	}

	private static RuntimeResult visitUnaryOperationNode(@NotNull UnaryOperationNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> value = result.register(visit(node.node, context)).setPosition(node.startPosition, node.endPosition);
		if (result.shouldReturn()) return result;

		if (node.operationToken.matches(TokenTypes.MINUS)) return value.mul(new NumberValue(-1, context));
		else if (node.operationToken.matches(TokenTypes.NOT))
			return result.success(new BooleanValue(!value.isTrue(), context));
		else return result.success(value);
	}

	private static RuntimeResult visitIfNode(@NotNull IfNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		Case[] cases = {};
		cases = node.cases.toArray(cases);

		for (Case ifCase : cases) {
			Node condition = ifCase.condition;
			Node body = ifCase.body;
			boolean shouldReturnNull = !ifCase.isExpression;

			Value<?> conditionValue = result.register(visit(condition, context));
			if (result.shouldReturn()) return result;

			if (conditionValue.isTrue()) {
				Value<?> expressionValue = result.register(visit(body, context));
				if (result.shouldReturn()) return result;

				return result.success(shouldReturnNull ? new NullValue(context) : expressionValue);
			}
		}

		if (node.elseCase != null) {
			Value<?> elseValue = result.register(visit(node.elseCase.body, context));
			if (result.shouldReturn()) return result;

			return result.success(node.elseCase.isExpression ? elseValue : new NullValue(context));
		}
		return result.success(new NullValue(context));
	}

	private static RuntimeResult visitObjectNode(@NotNull ObjectNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		Map<String, Value<?>> valueObj = new HashMap<>();
		ObjectValue objectValue = new ObjectValue(valueObj, context);

		for (Map.Entry<String, Node> value : node.object.entrySet()) {
			Value<?> objValue = result.register(visit(value.getValue(), context));
			if (result.shouldReturn()) return result;

			if (objValue instanceof BaseFunctionValue) ((BaseFunctionValue) objValue).setThis(objectValue);

			valueObj.put(value.getKey(), objValue);
		}

		objectValue.setPosition(node.startPosition, node.endPosition);
		return result.success(objectValue);
	}

	private static RuntimeResult visitObjectAccessNode(@NotNull ObjectAccessNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> object = result.register(visit(node.objectNode, context));
		if (result.shouldReturn()) return result;

		String key = node.keyToken != null ? node.keyToken.value : result.register(visit(node.keyNode, context)).getValueString();
		Position startPosition = node.keyToken != null ? node.keyToken.startPosition : node.keyNode.startPosition;
		Position endPosition = node.keyToken != null ? node.keyToken.endPosition : node.keyNode.endPosition;
		Value<?> returnValue = null;

		if (object instanceof ObjectValue) {

			if (result.shouldReturn()) return result;

			returnValue = ((ObjectValue) object).get(key);
		} else if (object instanceof ListValue) {
			Value<?> keyValue = result.register(visit(node.keyNode, context));
			if (result.shouldReturn()) return result;

			if (keyValue instanceof NumberValue) {
				returnValue = ((ListValue) object).value.get(Integer.parseInt(keyValue.getValueString()));
			}
		}
		if (returnValue == null) {
			object.makePrototype();
			returnValue = object.prototype.get(key);
		}

		return result.success(returnValue != null ? returnValue.setPosition(startPosition, endPosition) : new NullValue(context).setPosition(startPosition, endPosition));
	}

	private static RuntimeResult visitObjectAssignNode(@NotNull ObjectAssignNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> object = result.register(visit(node.objectNode, context));
		if (result.shouldReturn()) return result;

		if (!(object instanceof ObjectValue || object instanceof ListValue))
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, "Not an object/list", context));

		if (node.keyToken != null && object instanceof ListValue)
			return result.failure(new RuntimeError(node.keyToken.startPosition, node.keyToken.endPosition, "Not an object", context));

		Value<?> value = result.register(visit(node.valueNode, context));
		if (result.shouldReturn()) return result;

		if (object instanceof ObjectValue) {
			String key = node.keyNode == null ? node.keyToken.value : result.register(visit(node.keyNode, context)).getValueString();
			if (result.shouldReturn()) return result;

			((ObjectValue) object).set(key, value);
		} else {
			Value<?> keyValue = result.register(visit(node.keyNode, context));
			if (result.shouldReturn()) return result;

			if (!(keyValue instanceof NumberValue) || keyValue.getValueString().contains(".") || Integer.parseInt(keyValue.getValueString()) < 0 || Integer.parseInt(keyValue.getValueString()) > ((ListValue) object).value.size())
				return result.success(new NullValue(context));

			if (Integer.parseInt(keyValue.getValueString()) == ((ListValue) object).value.size())
				((ListValue) object).value.add(value);
			else ((ListValue) object).value.set(Integer.parseInt(keyValue.getValueString()), value);

		}
		return result.success(object);
	}

	private static RuntimeResult visitVarAccessNode(@NotNull VarAccessNode node, @NotNull Context context) {
		RuntimeResult result = new RuntimeResult();
		String varName = node.token.value;
		Value<?> value = context.symbolTable.get(varName);

		if (value == null)
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, varName + " is not defined", context));

		value = value.setPosition(node.startPosition, node.endPosition);
		return result.success(value);
	}

	private static RuntimeResult visitVarCreateNode(@NotNull VarCreateNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		String varName = node.token.value;

		Value<?> value = result.register(visit(node.expression, context));
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

		Value<?> value = result.register(visit(node.expression, context)).setPosition(node.token.startPosition, node.token.endPosition);
		if (result.shouldReturn()) return result;

		boolean successful = context.symbolTable.setExisting(varName, value);

		if (!successful) {
			if (!context.symbolTable.isConstantParents(varName)) {
				Krip.globals.put(varName, value);
				Krip.context.symbolTable.set(varName, value, false);
			} else
				return result.failure(new RuntimeError(node.startPosition, node.endPosition, "Variable is constant", context));
		}
		return result.success(value);
	}

	private static RuntimeResult visitForNode(@NotNull ForNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		List<Value<?>> elements = new ArrayList<>();

		Value<?> startValue = result.register(visit(node.startValueNode, context));
		if (result.shouldReturn()) return result;

		Value<?> endValue = result.register(visit(node.endValueNode, context));
		if (result.shouldReturn()) return result;

		if (!(startValue instanceof NumberValue && endValue instanceof NumberValue))
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, "Not a number", context));

		int i = Integer.parseInt(startValue.getValueString());

		if (i > Integer.parseInt(endValue.getValueString()))
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, "The start value mustn't be greater than the end value", context));

		Context forContext = new Context("for loop", context);
		forContext.symbolTable = new SymbolTable(context.symbolTable);

		while (i <= Integer.parseInt(endValue.getValueString())) {
			forContext.symbolTable.set(node.varNameToken.value, new NumberValue(i, forContext), false);
			i++;

			Value<?> value = result.register(visit(node.bodyNode, forContext));

			if (result.shouldReturn() && !result.loopShouldContinue && !result.loopShouldBreak) return result;

			if (result.loopShouldContinue) continue;
			if (result.loopShouldBreak) break;

			elements.add(value);
		}

		return result.success(node.shouldReturnNull ? new NullValue(context) : new ListValue(elements, context).setPosition(node.startPosition, node.endPosition));
	}

	private static RuntimeResult visitWhileNode(@NotNull WhileNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		List<Value<?>> elements = new ArrayList<>();

		while (true) {
			BooleanValue condition = (BooleanValue) result.register(visit(node.conditionNode, context));
			if (result.shouldReturn()) return result;

			if (!condition.isTrue()) break;

			Context whileContext = new Context("while loop", context);
			whileContext.symbolTable = new SymbolTable(context.symbolTable);

			Value<?> value = result.register(visit(node.bodyNode, whileContext));

			if (result.shouldReturn() && !result.loopShouldContinue && !result.loopShouldBreak) return result;

			if (result.loopShouldContinue) continue;
			if (result.loopShouldBreak) break;
			elements.add(value);
		}

		return result.success(node.shouldReturnNull ? new NullValue(context) : new ListValue(elements, context).setPosition(node.startPosition, node.endPosition));
	}

	@Contract(pure = true)
	private static RuntimeResult visitFunctionNode(@NotNull FunctionNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		String functionName = (node.varNameToken != null && node.varNameToken.value != null) ? node.varNameToken.value : null;

		Node body = node.bodyNode;

		List<String> argNames = new ArrayList<>();
		node.argNameTokens.forEach((token) -> argNames.add(token.value));

		Value<?> functionValue = new FunctionValue(functionName, body, argNames, node.shouldAutoReturn, context).setPosition(node.startPosition, node.endPosition);

		boolean success = true;
		if (node.varNameToken != null) success = context.symbolTable.set(functionName, functionValue, false);
		if (!success)
			return result.failure(new RuntimeError(node.startPosition, node.endPosition, "Cannot assign to constant variable", context));

		return result.success(functionValue);
	}

	private static RuntimeResult visitCallNode(@NotNull CallNode node, Context context) {
		RuntimeResult result = new RuntimeResult();

		Value<?> valueToCall = result.register(visit(node.nodeToCall, context));
		if (result.shouldReturn()) return result;

		if (!(valueToCall instanceof BaseFunctionValue))
			return result.failure(new RuntimeError(valueToCall.startPosition, valueToCall.endPosition, "Not a function", context));
		valueToCall.setPosition(node.startPosition, node.endPosition);

		Context callContext = new Context("<arguments>", context, node.startPosition);
		callContext.symbolTable = new SymbolTable(context.symbolTable);
		List<Value<?>> args = node.argNodes.stream().map(arg -> result.register(visit(arg, callContext))).collect(Collectors.toList());
		if (result.shouldReturn()) return result;

		Value<?> returnValue = result.register(((BaseFunctionValue) valueToCall).execute(args, context));
		if (result.shouldReturn()) return result;
		returnValue.setContext(context).setPosition(node.startPosition, node.endPosition);

		return result.success(returnValue);
	}

	private static RuntimeResult visitReturnNode(@NotNull ReturnNode node, Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> value = new NullValue(context);

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
