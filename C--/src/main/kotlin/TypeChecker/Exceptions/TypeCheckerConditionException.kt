package TypeChecker.Exceptions

import Parser.ParserToken.Type

class TypeCheckerConditionException(lineOfCode : Int,wrongType : Type) : TypeCheckerBaseException(lineOfCode, "Can't use type '$wrongType' in condition")