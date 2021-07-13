package TypeChecker.Exceptions

import Parser.ParserToken.*
import java.sql.Types

class TypeCheckerFunctionParameterException :Exception {
    constructor(functionName : String, parameters : List<Parameter>?, types: List<Type>?) : super("Function '$functionName' has to many or few parameter: [$parameters][$types]")

    constructor(functionName : String, parameters : Parameter, types: Type) : super("Function '$functionName' parameterType doesn't match given parameter [${parameters.type}] [$types]")

    constructor(message : String) : super(message)

}