package TypeChecker.Exceptions

import Parser.ParserToken.Type

class TypeCheckerConditionException(wrongType : Type) : Exception("Can't use type '$wrongType' in condition")