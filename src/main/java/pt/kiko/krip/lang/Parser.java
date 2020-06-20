package pt.kiko.krip.lang;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.cases.Case;
import pt.kiko.krip.lang.cases.Cases;
import pt.kiko.krip.lang.cases.ElseCase;
import pt.kiko.krip.lang.errors.SyntaxError;
import pt.kiko.krip.lang.nodes.*;
import pt.kiko.krip.lang.results.CaseResult;
import pt.kiko.krip.lang.results.ParseResult;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Responsible for checking the grammar and making a parse tree from a list of tokens
 */
public class Parser {

	private final ArrayList<Token> tokens;
	private int tokenIndex = -1;
	private Token currentToken;

	/**
	 * Makes a new instance
	 *
	 * @param tokens List of tokens to store
	 */
	public Parser(ArrayList<Token> tokens) {
		this.tokens = tokens;
		advance();
	}

	private void updateCurrentToken() {
		if (tokenIndex >= 0 && tokenIndex < tokens.size()) {
			currentToken = tokens.get(tokenIndex);
		}
	}

	private void advance() {
		tokenIndex++;
		updateCurrentToken();
	}

	private void advance(ParseResult result) {
		tokenIndex++;
		updateCurrentToken();
		while (currentToken.matches(TokenTypes.NEWLINE)) {
			tokenIndex++;
			updateCurrentToken();
			result.registerAdvancement();
		}
	}

	private void advance(CaseResult result) {
		tokenIndex++;
		updateCurrentToken();
		while (currentToken.matches(TokenTypes.NEWLINE)) {
			tokenIndex++;
			updateCurrentToken();
			result.registerAdvancement();
		}
	}

	private void advanceNewlines(ParseResult result) {
		while (currentToken.matches(TokenTypes.NEWLINE)) {
			tokenIndex++;
			updateCurrentToken();
			result.registerAdvancement();
		}
	}

	private void advanceNewlines(CaseResult result) {
		while (currentToken.matches(TokenTypes.NEWLINE)) {
			tokenIndex++;
			updateCurrentToken();
			result.registerAdvancement();
		}
	}

	private void reverse() {
		this.tokenIndex--;
		updateCurrentToken();
	}

	private void reverse(int amount) {
		this.tokenIndex -= amount;
		updateCurrentToken();
	}

	/**
	 * Parses the list of tokens
	 *
	 * @return A ParseResult instance
	 */
	public ParseResult parse() {
		ParseResult result = statements();

		if (result.error == null && !currentToken.matches(TokenTypes.EOF)) {
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Unexpected token: " + currentToken));
		}

		return result;
	}

	private ParseResult statements() {
		ParseResult result = new ParseResult();
		ArrayList<Node> statements = new ArrayList<>();
		Position startPosition = currentToken.startPosition.copy();

		advanceNewlines(result);

		if (currentToken.matches(TokenTypes.RBRACKET)) return result.success(new EmptyNode(currentToken.startPosition));

		Node statement = result.register(statement());
		if (result.error != null) return result;
		statements.add(statement);

		boolean moreStatements = true;

		while (true) {
			int newlineCount = 0;
			while (currentToken.type == TokenTypes.NEWLINE) {
				result.registerAdvancement();
				advance();
				newlineCount++;
			}
			if (newlineCount == 0) moreStatements = false;
			if (!moreStatements || currentToken.matches(TokenTypes.RBRACKET) || currentToken.matches(TokenTypes.RPAREN) || currentToken.matches(TokenTypes.EOF))
				break;

			statement = result.register(statement());
			if (result.error != null) {
				reverse(result.advanceCount);
				moreStatements = false;
				continue;
			}
			statements.add(statement);

		}
		if (result.error != null) return result;
		return result.success(new ListNode(statements, startPosition, currentToken.endPosition.copy()));
	}

	private ParseResult statement() {
		ParseResult result = new ParseResult();
		Position startPosition = currentToken.startPosition.copy();
		Node expression;

		if (currentToken.matches(TokenTypes.KEYWORD, "return")) {
			result.registerAdvancement();
			advance();
			expression = result.tryRegister(expression());
			if (expression == null) {
				reverse(result.toReverseCount);
			}
			return result.success(new ReturnNode(expression, startPosition, currentToken.endPosition));
		} else if (currentToken.matches(TokenTypes.KEYWORD, "continue")) {
			result.registerAdvancement();
			advance();
			return result.success(new ContinueNode(startPosition, currentToken.endPosition));
		} else if (currentToken.matches(TokenTypes.KEYWORD, "break")) {
			result.registerAdvancement();
			advance();
			return result.success(new BreakNode(startPosition, currentToken.endPosition));
		} else if (currentToken.matches(TokenTypes.KEYWORD, "try")) {
			Position endPosition;

			result.registerAdvancement();
			advance(result);

			if (!currentToken.matches(TokenTypes.LBRACKET))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '{'"));

			result.registerAdvancement();
			advance(result);

			Node tryStatements = result.register(statements());
			if (result.error != null) return result;

			if (!currentToken.matches(TokenTypes.RBRACKET))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '}'"));

			result.registerAdvancement();
			advance(result);

			if (!currentToken.matches(TokenTypes.KEYWORD, "catch"))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected 'catch'"));

			result.registerAdvancement();
			advance();

			Token errorNameToken = null;

			if (currentToken.matches(TokenTypes.LPAREN)) {
				result.registerAdvancement();
				advance(result);

				if (!currentToken.matches(TokenTypes.IDENTIFIER))
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Unexpected token: " + currentToken));
				errorNameToken = currentToken;

				result.registerAdvancement();
				advance(result);

				if (!currentToken.matches(TokenTypes.RPAREN))
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ')'"));

				result.registerAdvancement();
				advance();
			}

			if (!currentToken.matches(TokenTypes.LBRACKET))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '{'"));

			result.registerAdvancement();
			advance(result);

			Node catchStatements = result.register(statements());
			if (result.error != null) return result;

			if (!currentToken.matches(TokenTypes.RBRACKET))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '}'"));

			endPosition = currentToken.endPosition;

			result.registerAdvancement();
			advance();

			Node finallyStatements = null;
			if (currentToken.matches(TokenTypes.KEYWORD, "finally")) {
				result.registerAdvancement();
				advance(result);

				if (!currentToken.matches(TokenTypes.LBRACKET))
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '{'"));

				result.registerAdvancement();
				advance(result);

				finallyStatements = result.register(statements());
				if (result.error != null) return result;

				if (!currentToken.matches(TokenTypes.RBRACKET))
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '}'"));

				endPosition = currentToken.endPosition;

				result.registerAdvancement();
				advance();
			}

			return result.success(new TryCatchNode(startPosition, endPosition, tryStatements, catchStatements, errorNameToken, finallyStatements));
		}
		expression = result.register(expression());
		if (result.error != null) return result;
		return result.success(expression);
	}

	private ParseResult expression() {
		ParseResult result = new ParseResult();

		if (currentToken.matches(TokenTypes.KEYWORD, "let") || currentToken.matches(TokenTypes.KEYWORD, "const")) {
			boolean isFinal = currentToken.matches(TokenTypes.KEYWORD, "const");

			result.registerAdvancement();
			advance();

			if (!currentToken.matches(TokenTypes.IDENTIFIER))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Unexpected identifier"));

			Token varNameToken = currentToken;
			result.registerAdvancement();
			advance();

			if (!currentToken.matches(TokenTypes.EQ)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '='"));

			result.registerAdvancement();
			advance();

			Node expression = result.register(expression());
			if (result.error != null) return result;

			return result.success(new VarCreateNode(varNameToken, expression, isFinal));
		}

		if (currentToken.matches(TokenTypes.IDENTIFIER)) {
			Token varNameToken = currentToken;

			result.registerAdvancement();
			advance();

			if (!currentToken.matches(TokenTypes.EQ) && !currentToken.matches(TokenTypes.PLUS) && !currentToken.matches(TokenTypes.MINUS) && !currentToken.matches(TokenTypes.MUL) && !currentToken.matches(TokenTypes.DIV)) {
				reverse();
				result.advanceCount--;
			} else {
				if (!currentToken.matches(TokenTypes.EQ) && !currentToken.matches(TokenTypes.MUL) && !currentToken.matches(TokenTypes.DIV)) {
					Token token = currentToken;

					result.registerAdvancement();
					advance();

					if (!currentToken.matches(token.type)) {
						reverse();
						result.advanceCount--;
					} else {
						result.registerAdvancement();
						advance();

						return result.success(new VarAssignNode(varNameToken, new BinaryOperationNode(new VarAccessNode(varNameToken), token, new NumberNode(new Token(TokenTypes.NUMBER, "1", token.startPosition)))));
					}
				}
				if (!currentToken.matches(TokenTypes.EQ)) {
					Token token = currentToken;

					result.registerAdvancement();
					advance();

					if (!currentToken.matches(TokenTypes.EQ)) {
						reverse(2);
						result.advanceCount -= 2;
					} else {
						result.registerAdvancement();
						advance();

						Node right = result.register(expression());
						if (result.error != null) return result;

						return result.success(new VarAssignNode(varNameToken, new BinaryOperationNode(new VarAccessNode(varNameToken), token, right)));
					}
				} else {
					result.registerAdvancement();
					advance();

					Node expression = result.register(expression());
					if (result.error != null) return result;

					return result.success(new VarAssignNode(varNameToken, expression));

				}
			}
		}

		Node node = result.register(binaryOperation(this::comparisonExpression, new TokenTypes[]{TokenTypes.AND, TokenTypes.OR}));
		if (result.error != null) return result;

		return result.success(node);
	}

	private ParseResult comparisonExpression() {
		return binaryOperation(this::arithmeticExpression, new TokenTypes[]{TokenTypes.EE, TokenTypes.NE, TokenTypes.LT, TokenTypes.LTE, TokenTypes.GT, TokenTypes.GTE});
	}

	private ParseResult arithmeticExpression() {
		ParseResult result = new ParseResult();

		if (currentToken.matches(TokenTypes.NOT)) {
			Token operationToken = currentToken;
			result.registerAdvancement();
			advance();

			Node node = result.register(arithmeticExpression());
			if (result.error != null) return result;
			return result.success(new UnaryOperationNode(operationToken, node));
		}
		return binaryOperation(this::term, new TokenTypes[]{TokenTypes.PLUS, TokenTypes.MINUS});
	}

	private ParseResult term() {
		return binaryOperation(this::factor, new TokenTypes[]{TokenTypes.MUL, TokenTypes.DIV, TokenTypes.MOD});
	}

	private ParseResult factor() {
		ParseResult result = new ParseResult();
		Token token = currentToken;

		if (currentToken.matches(TokenTypes.PLUS) || currentToken.matches(TokenTypes.MINUS)) {
			result.registerAdvancement();
			advance();

			Node factor = result.register(factor());
			if (result.error != null) return result;

			return result.success(new UnaryOperationNode(token, factor));
		}
		return power();
	}

	private ParseResult power() {
		return binaryOperation(this::objectAccess, new TokenTypes[]{TokenTypes.POW});
	}

	private ParseResult objectAccess() {
		ParseResult result = new ParseResult();
		Node call = result.register(call());
		if (result.error != null) return result;
		int count = 0;

		while (currentToken.matches(TokenTypes.NEWLINE)) {
			result.registerAdvancement();
			advance();
			count++;
		}

		if (!currentToken.matches(TokenTypes.PERIOD) && !currentToken.matches(TokenTypes.LSQUARE)) reverse(count);

		while (currentToken.matches(TokenTypes.PERIOD) || currentToken.matches(TokenTypes.LSQUARE)) {
			result.registerAdvancement();
			if (currentToken.matches(TokenTypes.PERIOD)) {
				advance();

				if (!currentToken.matches(TokenTypes.IDENTIFIER))
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Unexpected identifier"));

				Token key = currentToken;

				result.registerAdvancement();
				advance();

				if (currentToken.matches(TokenTypes.EQ)) {
					result.registerAdvancement();
					advance();

					Node value = result.register(expression());
					if (result.error != null) return result;

					return result.success(new ObjectAssignNode(call, key, value));
				}

				call = new ObjectAccessNode(call, key);
			} else {
				advance(result);

				Node key = result.register(expression());
				if (result.error != null) return result;

				if (!currentToken.matches(TokenTypes.RSQUARE))
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ']'"));

				result.registerAdvancement();
				advance();

				if (currentToken.matches(TokenTypes.EQ)) {
					result.registerAdvancement();
					advance();

					Node value = result.register(expression());
					if (result.error != null) return result;

					return result.success(new ObjectAssignNode(call, key, value));
				}

				call = new ObjectAccessNode(call, key);
			}
			if (currentToken.matches(TokenTypes.LPAREN)) call = result.register(callSyntax(result, call));

			count = 0;
			while (currentToken.matches(TokenTypes.NEWLINE)) {
				result.registerAdvancement();
				advance();
				count++;
			}
			if (!currentToken.matches(TokenTypes.PERIOD) && !currentToken.matches(TokenTypes.LSQUARE)) reverse(count);
		}

		return result.success(call);
	}

	private ParseResult call() {
		ParseResult result = new ParseResult();
		Node atom = result.register(atom());
		if (result.error != null) return result;
		Node node = result.register(callSyntax(result, atom));
		if (result.error != null) return result;

		if (node == null) return result.success(atom);
		else return result.success(node);
	}

	private ParseResult callSyntax(ParseResult result, Node atom) {
		Node node = null;
		while (currentToken.matches(TokenTypes.LPAREN)) {
			result.registerAdvancement();
			advance(result);
			ArrayList<Node> argNodes = new ArrayList<>();

			if (currentToken.matches(TokenTypes.EOF))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ')' or identifier"));
			else if (!currentToken.matches(TokenTypes.RPAREN)) {
				argNodes.add(result.register(expression()));
				if (result.error != null)
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Unexpected token"));

				while (currentToken.matches(TokenTypes.COMMA)) {
					result.registerAdvancement();
					advance(result);

					argNodes.add(result.register(expression()));
					if (result.error != null) return result;
				}
				advanceNewlines(result);
				if (!currentToken.matches(TokenTypes.RPAREN))
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ')'"));

			}
			result.registerAdvancement();
			advance();
			if (node == null) node = new CallNode(atom, argNodes);
			else node = new CallNode(node, argNodes);
		}
		return result.success(node);
	}

	private ParseResult atom() {
		ParseResult result = new ParseResult();

		if (currentToken.matches(TokenTypes.NUMBER)) {
			Token token = currentToken;
			result.registerAdvancement();
			advance();
			return result.success(new NumberNode(token));
		} else if (currentToken.matches(TokenTypes.STRING)) {
			Token token = currentToken;
			result.registerAdvancement();
			advance();
			return result.success(new StringNode(token));
		} else if (currentToken.matches(TokenTypes.IDENTIFIER)) {
			Token token = currentToken;
			result.registerAdvancement();
			advance();
			if (!currentToken.matches(TokenTypes.ARROW)) return result.success(new VarAccessNode(token));
			else {
				result.advanceCount--;
				reverse();
				Node functionExpression = result.register(functionExpression());
				if (result.error != null) return result;
				return result.success(functionExpression);
			}
		} else if (currentToken.matches(TokenTypes.LBRACKET)) {
			Node objectExpression = result.register(objectExpression());
			if (result.error != null) return result;
			return result.success(objectExpression);
		} else if (currentToken.matches(TokenTypes.LSQUARE)) {
			Node listExpression = result.register(listExpression());
			if (result.error != null) return result;
			return result.success(listExpression);
		} else if (currentToken.matches(TokenTypes.KEYWORD, "if")) {
			Node ifExpression = result.register(ifExpression());
			if (result.error != null) return result;
			return result.success(ifExpression);
		} else if (currentToken.matches(TokenTypes.KEYWORD, "for")) {
			Node forExpression = result.register(forExpression());
			if (result.error != null) return result;
			return result.success(forExpression);
		} else if (currentToken.matches(TokenTypes.KEYWORD, "while")) {
			Node whileExpression = result.register(whileExpression());
			if (result.error != null) return result;
			return result.success(whileExpression);
		} else if (currentToken.matches(TokenTypes.KEYWORD, "function") || currentToken.matches(TokenTypes.LPAREN)) {
			Node functionExpression = result.register(functionExpression());
			if (result.error != null) return result;
			return result.success(functionExpression);
		} else if (currentToken.matches(TokenTypes.EOF)) return result;
		return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Unexpected token: " + currentToken));
	}

	// -------------------------------

	// <OBJECT EXPRESSION>

	private ParseResult objectExpression() {
		ParseResult result = new ParseResult();
		Position startPosition = currentToken.startPosition.copy();

		if (!currentToken.matches(TokenTypes.LBRACKET))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '{'"));

		result.registerAdvancement();
		advance(result);

		Map<String, Node> object = new HashMap<>();

		while (currentToken.matches(TokenTypes.IDENTIFIER) || currentToken.matches(TokenTypes.STRING)) {
			String key = currentToken.value;
			result.registerAdvancement();
			advance(result);

			if (!currentToken.matches(TokenTypes.COLON)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ':'"));

			result.registerAdvancement();
			advance(result);

			Node value = result.register(expression());
			if (result.error != null) return result;

			object.put(key, value);

			advanceNewlines(result);

			if (!currentToken.matches(TokenTypes.COMMA)) break;

			result.registerAdvancement();
			advance(result);
		}

		if (!currentToken.matches(TokenTypes.RBRACKET)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '}' or ','"));

		result.registerAdvancement();
		advance();

		return result.success(new ObjectNode(object, startPosition, currentToken.endPosition));
	}

	// </OBJECT EXPRESSION>

	// <LIST EXPRESSION>

	private ParseResult listExpression() {
		ParseResult result = new ParseResult();
		ArrayList<Node> nodes = new ArrayList<>();
		Position startPosition = currentToken.startPosition.copy();

		if (!currentToken.matches(TokenTypes.LSQUARE))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '['"));

		result.registerAdvancement();
		advance(result);

		if (!currentToken.matches(TokenTypes.RSQUARE)) {
			nodes.add(result.register(expression()));
			if (result.error != null) return result;

			advanceNewlines(result);

			while (currentToken.matches(TokenTypes.COMMA)) {
				result.registerAdvancement();
				advance(result);
				if (currentToken.matches(TokenTypes.RSQUARE)) break;

				nodes.add(result.register(expression()));
				if (result.error != null) return result;
			}

			advanceNewlines(result);

			if (!currentToken.matches(TokenTypes.RSQUARE))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ']'"));

		}
		result.registerAdvancement();
		advance();

		return result.success(new ListNode(nodes, startPosition, currentToken.endPosition));
	}

	// </LIST EXPRESSION>

	// <IF EXPRESSIONS>

	private ParseResult ifExpression() {
		CaseResult result = new CaseResult();
		Cases allCases = result.register(ifExpressionCases("if"));
		if (result.error != null) return new ParseResult().failure(result.error);

		if (allCases.elseCase == null) return new ParseResult().success(new IfNode(allCases.cases));
		else return new ParseResult().success(new IfNode(allCases.cases, allCases.elseCase));
	}

	private CaseResult ifExpressionCases(String caseKeyword) {
		CaseResult result = new CaseResult();
		List<Case> cases = new ArrayList<>();
		ElseCase elseCase = null;

		if (!currentToken.matches(TokenTypes.KEYWORD, caseKeyword))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '" + caseKeyword + "'"));

		result.registerAdvancement();
		advance();

		if (!currentToken.matches(TokenTypes.LPAREN))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '('"));

		result.registerAdvancement();
		advance(result);

		Node condition = result.register(expression());
		if (result.error != null) return result;

		advanceNewlines(result);

		if (!currentToken.matches(TokenTypes.RPAREN))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ')'"));

		result.registerAdvancement();
		advance(result);

		if (currentToken.matches(TokenTypes.LBRACKET)) {
			result.registerAdvancement();
			advance();

			Node statements = result.register(statements());
			if (result.error != null) return result;

			cases.add(new Case(condition, statements, false));

			if (!currentToken.matches(TokenTypes.RBRACKET))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '}'"));

			result.registerAdvancement();
			advance();

			AtomicInteger integer = new AtomicInteger();

			while (currentToken.matches(TokenTypes.NEWLINE)) {
				result.registerAdvancement();
				advance();
				integer.getAndIncrement();
			}

			if (currentToken.matches(TokenTypes.KEYWORD, "else if") || currentToken.matches(TokenTypes.KEYWORD, "else")) {
				Cases allCases = result.register(elseOrElseIfExpression());

				if (allCases.cases != null && allCases.cases.size() > 0) cases.addAll(allCases.cases);
				elseCase = allCases.elseCase;
			} else {
				result.advanceCount -= integer.get();
				reverse(integer.get());
			}
		} else {
			advanceNewlines(result);

			Node expression = result.register(statement());
			if (result.error != null) return result;
			cases.add(new Case(condition, expression, true));

			AtomicInteger integer = new AtomicInteger();

			while (currentToken.matches(TokenTypes.NEWLINE)) {
				result.registerAdvancement();
				advance();
				integer.getAndIncrement();
			}

			if (currentToken.matches(TokenTypes.KEYWORD, "else if") || currentToken.matches(TokenTypes.KEYWORD, "else")) {
				Cases allCases = result.register(elseOrElseIfExpression());

				if (allCases.cases != null && allCases.cases.size() > 0) cases.addAll(allCases.cases);
				elseCase = allCases.elseCase;
			} else {
				result.advanceCount -= integer.get();
				reverse(integer.get());
			}
		}
		return result.success(new Cases(cases, elseCase));
	}

	private CaseResult elseIfExpression() {
		return ifExpressionCases("else if");
	}

	private CaseResult elseExpression() {
		CaseResult result = new CaseResult();
		Cases cases = new Cases();

		result.registerAdvancement();
		advance(result);

		if (currentToken.matches(TokenTypes.LBRACKET)) {
			result.registerAdvancement();
			advance();

			Node statements = result.register(statements());
			if (result.error != null) return result;

			cases.setElseCase(new ElseCase(statements, false));

			if (!currentToken.matches(TokenTypes.RBRACKET)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '}'"));

			result.registerAdvancement();
			advance();
		} else {
			advanceNewlines(result);

			Node expression = result.register(statement());
			if (result.error != null) return result;
			cases.setElseCase(new ElseCase(expression, true));
		}
		return result.success(cases);
	}

	private CaseResult elseOrElseIfExpression() {
		CaseResult result = new CaseResult();
		Cases cases = new Cases();

		if (currentToken.matches(TokenTypes.KEYWORD, "else if")) {
			cases = result.register(elseIfExpression());
		} else {
			cases.setElseCase(result.register(elseExpression()).elseCase);
		}
		if (result.error != null) return result;
		return result.success(cases);
	}

	// </IF EXPRESSIONS>

	// <FOR EXPRESSION>

	private ParseResult forExpression() {
		ParseResult result = new ParseResult();

		if (!currentToken.matches(TokenTypes.KEYWORD, "for"))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected 'for'"));

		result.registerAdvancement();
		advance();

		if (!currentToken.matches(TokenTypes.LPAREN))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '('"));

		result.registerAdvancement();
		advance();

		if (!currentToken.matches(TokenTypes.IDENTIFIER))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Unexpected identifier"));

		Token varNameToken = currentToken;
		result.registerAdvancement();
		advance();

		if (!currentToken.matches(TokenTypes.EQ))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '='"));

		result.registerAdvancement();
		advance();

		Node startValue = result.register(expression());
		if (result.error != null) return result;

		if (!currentToken.matches(TokenTypes.KEYWORD, "to")) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected 'to'"));

		result.registerAdvancement();
		advance();

		Node endValue = result.register(expression());
		if (result.error != null) return result;

		if (!currentToken.matches(TokenTypes.RPAREN)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ')'"));

		result.registerAdvancement();
		advance();

		if (currentToken.matches(TokenTypes.LBRACKET)) {
			result.registerAdvancement();
			advance();

			Node body = result.register(statements());
			if (result.error != null) return result;

			if (!currentToken.matches(TokenTypes.RBRACKET)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '}'"));

			result.registerAdvancement();
			advance();

			return result.success(new ForNode(varNameToken, startValue, endValue, body, true));
		} else {
			Node body = result.register(statement());
			if (result.error != null) return result;

			return result.success(new ForNode(varNameToken, startValue, endValue, body, false));
		}
	}

	// </FOR EXPRESSION>

	// <WHILE EXPRESSION>

	private ParseResult whileExpression() {
		ParseResult result = new ParseResult();

		if (!currentToken.matches(TokenTypes.KEYWORD, "while"))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected 'while'"));

		result.registerAdvancement();
		advance();

		if (!currentToken.matches(TokenTypes.LPAREN))
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '('"));

		result.registerAdvancement();
		advance();

		Node condition = result.register(expression());
		if (result.error != null) return result;

		if (!currentToken.matches(TokenTypes.RPAREN)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ')'"));

		result.registerAdvancement();
		advance();

		if (currentToken.matches(TokenTypes.LBRACKET)) {
			result.registerAdvancement();
			advance();

			Node body = result.register(statements());
			if (result.error != null) return result;

			if (!currentToken.matches(TokenTypes.RBRACKET)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '}'"));

			result.registerAdvancement();
			advance();

			return result.success(new WhileNode(condition, body, true));
		} else {
			Node body = result.register(statement());
			if (result.error != null) return result;

			return result.success(new WhileNode(condition, body, false));
		}
	}

	// </WHILE EXPRESSION>

	// <FUNCTION EXPRESSION>

	private ParseResult functionExpression() {
		ParseResult result = new ParseResult();
		Token varNameToken = null;
		boolean isArrowFunction = false;
		boolean singleArgument = false;

		if (currentToken.matches(TokenTypes.LPAREN)) {
			while (!(currentToken.matches(TokenTypes.RPAREN) || currentToken.matches(TokenTypes.EOF))) {
				result.registerAdvancement();
				advance();
			}

			if (currentToken.matches(TokenTypes.EOF)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ')'"));

			result.registerAdvancement();
			advance();

			isArrowFunction = currentToken.matches(TokenTypes.ARROW);

			reverse(result.advanceCount);
			result.advanceCount = 0;

			if (!isArrowFunction) {
				result.registerAdvancement();
				advance();

				Node expression = result.register(expression());
				if (result.error != null) return result;

				if (!currentToken.matches(TokenTypes.RPAREN)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ')'"));

				result.registerAdvancement();
				advance();

				return result.success(expression);
			}
		} else if (currentToken.matches(TokenTypes.IDENTIFIER)) {
			result.registerAdvancement();
			advance();

			isArrowFunction = currentToken.matches(TokenTypes.ARROW);
			if (isArrowFunction) singleArgument = true;

			result.advanceCount--;
			reverse();
		} else if (currentToken.matches(TokenTypes.KEYWORD, "function")) {
			result.registerAdvancement();
			advance();
		} else return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected identifier, '(' or 'function'"));

		if (!isArrowFunction) {
			if (currentToken.matches(TokenTypes.IDENTIFIER)) {
				varNameToken = currentToken;

				result.registerAdvancement();
				advance();

				if (!currentToken.matches(TokenTypes.LPAREN))
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '('"));
			} else if (!currentToken.matches(TokenTypes.LPAREN))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected identifier or '('"));
		}

		if (!singleArgument) {
			result.registerAdvancement();
			advance();
		}

		List<Token> argNameTokens = new ArrayList<>();

		if (currentToken.matches(TokenTypes.IDENTIFIER)) {
			argNameTokens.add(currentToken);

			result.registerAdvancement();
			advance();

			while (currentToken.matches(TokenTypes.COMMA)) {
				result.registerAdvancement();
				advance();

				if (!currentToken.matches(TokenTypes.IDENTIFIER))
					return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Unexpected identifier"));

				argNameTokens.add(currentToken);
				result.registerAdvancement();
				advance();
			}

			if (!currentToken.matches(TokenTypes.RPAREN) && !singleArgument) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected ')'"));
		} else if (!currentToken.matches(TokenTypes.RPAREN)) return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected identifier or ')'"));

		result.registerAdvancement();
		advance();

		if (isArrowFunction && !singleArgument) {
			result.registerAdvancement();
			advance();
		}
		if (currentToken.matches(TokenTypes.LBRACKET)) {
			result.registerAdvancement();
			advance();

			Node body = new EmptyNode(currentToken.startPosition);
			if (!currentToken.matches(TokenTypes.RBRACKET)) {
				body = result.register(statements());
				if (result.error != null) return result;
			}

			if (!currentToken.matches(TokenTypes.RBRACKET))
				return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '}'"));

			result.registerAdvancement();
			advance();

			return result.success(new FunctionNode(body, false, varNameToken, argNameTokens));
		} else if (isArrowFunction) {
			advanceNewlines(result);
			Node bodyNode = result.register(expression());
			if (result.error != null) return result;


			return result.success(new FunctionNode(bodyNode, true, null, argNameTokens));
		} else
			return result.failure(new SyntaxError(currentToken.startPosition, currentToken.endPosition, "Expected '=>' or '{'"));
	}

	// </FUNCTION EXPRESSION>

	// -------------------------------

	private ParseResult binaryOperation(@NotNull ParseRunnable func, TokenTypes[] types) {
		ParseResult result = new ParseResult();
		Node left = result.register(func.run());
		if (result.error != null) return result;
		int count = 0;

		while (currentToken.matches(TokenTypes.NEWLINE)) {
			result.registerAdvancement();
			advance();
			count++;
		}
		if (!Arrays.asList(types).contains(currentToken.type)) {
			result.advanceCount -= count;
			reverse(count);
		}
		while (Arrays.asList(types).contains(currentToken.type)) {
			Token operationToken = currentToken;

			result.registerAdvancement();
			advance(result);

			Node right = result.register(func.run());
			if (result.error != null) return result;
			left = new BinaryOperationNode(left, operationToken, right);
		}
		return result.success(left);
	}
}
