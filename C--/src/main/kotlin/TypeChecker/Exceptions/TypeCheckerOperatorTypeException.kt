package TypeChecker.Exceptions

import Parser.ParserToken.*

class TypeCheckerOperatorTypeException: TypeCheckerBaseException{

    constructor(lineOfCode : Int,operator : Operator, type: Type) : super(lineOfCode, "Can't use type '$type' with operator '$operator'")

    constructor(lineOfCode : Int,operator : Operator, typeA: Type, typeB: Type) : super(lineOfCode, "Can't use type '$typeA' and '$typeB' with operator '$operator'")

}