package TypeChecker.Exceptions

import Parser.ParserToken.*

class TypeCheckerOperatorTypeException: Exception{

    constructor(operator : Operator, type: Type) : super("Can't use type '$type' with operator '$operator'")

    constructor(operator : Operator, typeA: Type, typeB: Type) : super("Can't use type '$typeA' and '$typeB' with operator '$operator'")

}